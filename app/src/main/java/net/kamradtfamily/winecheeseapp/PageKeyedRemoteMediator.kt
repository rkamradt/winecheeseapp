package net.kamradtfamily.winecheeseapp

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(val db: WineCheeseDb,
                              val wineCheeseApi: WineCheeseApi,
                              val id: Int)  : RemoteMediator<Int, WineCheesePost>() {
    private val postDao: WineCheesePostDao = db.posts()
    private val remoteKeyDao: WineCheeseRemoteKeyDao = db.remoteKeys()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, WineCheesePost>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val loadKey = when (loadType) {
                REFRESH -> null
                PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.
                    val remoteKey = db.withTransaction {
                        remoteKeyDao.remoteKeyByPost(id)
                    }

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.
                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            }

            val data = wineCheeseApi.getTop(
                id = id,
                after = loadKey,
                before = null,
                limit = when (loadType) {
                    REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }
            ).data

            val items = data.children.map { it.data }

            db.withTransaction {
                if (loadType == REFRESH) {
                    postDao.deleteBySubreddit(id)
                    remoteKeyDao.deleteBySubreddit(id)
                }

                remoteKeyDao.insert(WineCheeseRemoteKey(id, data.after))
                postDao.insertAll(items)
            }

            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }


}

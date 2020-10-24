package net.kamradtfamily.winecheeseapp

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class ItemKeyedWineCheesePagingSource(val wineCheeseApi: WineCheeseApi, val id: Int)  : PagingSource<Int, WineCheesePost>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WineCheesePost> {
        return try {
            val items = wineCheeseApi.getTop(
                id = id,
                after = if (params is LoadParams.Append) params.key else null,
                before = if (params is LoadParams.Prepend) params.key else null,
                limit = params.loadSize
            ).data.children.map { it.data }

            LoadResult.Page(
                data = items,
                prevKey = items.firstOrNull()?.id,
                nextKey = items.lastOrNull()?.id
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRefreshKey(state: PagingState<Int, WineCheesePost>): Int? {
        /**
         * The id field is a unique identifier for post items.
         * (no it is not the title of the post :) )
         * https://www.reddit.com/dev/api
         */
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }


}

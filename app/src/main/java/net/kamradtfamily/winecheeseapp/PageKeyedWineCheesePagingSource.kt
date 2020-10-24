package net.kamradtfamily.winecheeseapp

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Prepend
import androidx.paging.PagingSource.LoadResult.Page
import retrofit2.HttpException
import java.io.IOException


class PageKeyedWineCheesePagingSource(
private val wineCheeseApi: WineCheeseApi,
private val id: Int
) : PagingSource<Int, WineCheesePost>() {
    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, WineCheesePost> {
        return try {
            val data = wineCheeseApi.getTop(
                id = id,
                after = if (params is Append) params.key else null,
                before = if (params is Prepend) params.key else null,
                limit = params.loadSize
            ).data

            Page(
                data = data.children.map { it.data },
                prevKey = data.before,
                nextKey = data.after
            )
        } catch (e: IOException) {
            PagingSource.LoadResult.Error(e)
        } catch (e: HttpException) {
            PagingSource.LoadResult.Error(e)
        }
    }


}

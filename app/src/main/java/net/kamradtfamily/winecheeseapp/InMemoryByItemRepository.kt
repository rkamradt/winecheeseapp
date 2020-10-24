package net.kamradtfamily.winecheeseapp

import androidx.paging.Pager
import androidx.paging.PagingConfig

class InMemoryByItemRepository(val wineCheeseApi: WineCheeseApi) : WineCheesePostRepository {
    override fun postsOfWineCheese(id: Int, pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        )
    ) {
        ItemKeyedWineCheesePagingSource(
            wineCheeseApi = wineCheeseApi,
            id = id
        )
    }.flow

}

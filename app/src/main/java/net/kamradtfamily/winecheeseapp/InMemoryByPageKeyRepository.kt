package net.kamradtfamily.winecheeseapp

import androidx.paging.Pager
import androidx.paging.PagingConfig

class InMemoryByPageKeyRepository(val wineCheeseApi: WineCheeseApi) :
    WineCheesePostRepository {
    override fun postsOfWineCheese(id: Int, pageSize: Int) = Pager(
        PagingConfig(pageSize)
    ) {
        PageKeyedWineCheesePagingSource(
            wineCheeseApi = wineCheeseApi,
            id = id
        )
    }.flow

}

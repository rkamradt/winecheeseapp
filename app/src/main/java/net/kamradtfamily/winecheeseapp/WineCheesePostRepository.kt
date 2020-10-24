package net.kamradtfamily.winecheeseapp

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface WineCheesePostRepository {
    fun postsOfWineCheese(id: Int, pageSize: Int): Flow<PagingData<WineCheesePost>>
    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB

    }
}

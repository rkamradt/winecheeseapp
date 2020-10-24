package net.kamradtfamily.winecheeseapp

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WineCheesePostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<WineCheesePost>)

    @Query("SELECT * FROM winecheese WHERE id = :id ORDER BY indexInResponse ASC")
    fun postsByWineCheese(id: Int): PagingSource<Int, WineCheesePost>

    @Query("DELETE FROM winecheese WHERE id = :id")
    suspend fun deleteBySubreddit(id: Int)

    @Query("SELECT MAX(indexInResponse) + 1 FROM winecheese WHERE id = :id")
    suspend fun getNextIndexInWineCheese(id: Int): Int

}

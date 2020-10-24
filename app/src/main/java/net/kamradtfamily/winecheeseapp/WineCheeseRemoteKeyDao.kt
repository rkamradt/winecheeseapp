package net.kamradtfamily.winecheeseapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WineCheeseRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: WineCheeseRemoteKey)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeyByPost(id: Int): WineCheeseRemoteKey

    @Query("DELETE FROM remote_keys WHERE id = :id")
    suspend fun deleteBySubreddit(id: Int)

}

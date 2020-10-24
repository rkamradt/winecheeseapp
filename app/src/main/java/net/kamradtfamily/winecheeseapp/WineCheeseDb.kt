package net.kamradtfamily.winecheeseapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WineCheesePost::class, WineCheeseRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class WineCheeseDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): WineCheeseDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, WineCheeseDb::class.java)
            } else {
                Room.databaseBuilder(context, WineCheeseDb::class.java, "winecheese.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun posts(): WineCheesePostDao
    abstract fun remoteKeys(): WineCheeseRemoteKeyDao


}

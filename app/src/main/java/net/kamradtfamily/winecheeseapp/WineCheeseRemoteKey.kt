package net.kamradtfamily.winecheeseapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class WineCheeseRemoteKey(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val id: Int, // technically mutable but fine for a demo
    val nextPageKey: Int?
)

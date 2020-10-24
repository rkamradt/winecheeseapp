package net.kamradtfamily.winecheeseapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "winecheese",
    indices = [Index(value = ["id"], unique = false)])
data class WineCheesePost (
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("wine")
    val wine: String,
    @SerializedName("wine_description")
    val wine_description: String,
    @SerializedName("cheese")
    val cheese: String,
    @SerializedName("cheese_description")
    val cheese_description: String,
    @SerializedName("pairing")
    val pairing: String) {
        // to be consistent w/ changing backend order, we need to keep a data like this
        var indexInResponse: Int = -1

}

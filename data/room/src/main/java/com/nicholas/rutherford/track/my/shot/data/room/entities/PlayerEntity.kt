package com.nicholas.rutherford.track.my.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.my.shot.data.room.response.Player
import com.nicholas.rutherford.track.my.shot.data.room.response.PlayerPositions

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "position")
    val position: PlayerPositions,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?
)

fun PlayerEntity.toPlayer(): Player {
    return Player(
        id = id,
        firstName = firstName,
        lastName = lastName,
        position = position,
        imageUrl = imageUrl
    )
}
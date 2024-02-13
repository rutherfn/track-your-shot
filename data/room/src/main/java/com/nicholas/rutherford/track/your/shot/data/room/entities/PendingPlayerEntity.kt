package com.nicholas.rutherford.track.your.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

@Entity(tableName = "pendingPlayers")
data class PendingPlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "lastName")
    val lastName: String,
    @ColumnInfo(name = "position")
    val position: PlayerPositions,
    @ColumnInfo(name = "firebaseKey")
    val firebaseKey: String,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,
    @ColumnInfo(name = "shotsLogged")
    val shotsLoggedList: List<ShotLogged>
)

fun PendingPlayerEntity.toPlayer(): Player {
    return Player(
        firstName = firstName,
        lastName = lastName,
        position = position,
        firebaseKey = firebaseKey,
        imageUrl = imageUrl,
        shotsLoggedList = shotsLoggedList
    )
}

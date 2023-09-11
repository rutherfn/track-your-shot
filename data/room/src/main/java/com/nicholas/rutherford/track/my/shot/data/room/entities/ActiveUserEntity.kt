package com.nicholas.rutherford.track.my.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.my.shot.data.room.response.ActiveUser

@Entity(tableName = "activeUsers")
data class ActiveUserEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "accountHasBeenCreated")
    val accountHasBeenCreated: Boolean,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "firebaseAccountInfoKey")
    val firebaseAccountInfoKey: String
)

fun ActiveUserEntity.toActiveUser(): ActiveUser {
    return ActiveUser(
        id = id,
        accountHasBeenCreated = accountHasBeenCreated,
        email = email,
        username = username,
        firebaseAccountInfoKey = firebaseAccountInfoKey
    )
}

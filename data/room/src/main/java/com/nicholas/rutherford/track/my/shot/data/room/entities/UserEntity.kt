package com.nicholas.rutherford.track.my.shot.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nicholas.rutherford.track.my.shot.data.room.response.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "username")
    val username: String
)

fun UserEntity.toUser(): User {
    return User(
        id = id,
        email = email,
        username = username
    )
}

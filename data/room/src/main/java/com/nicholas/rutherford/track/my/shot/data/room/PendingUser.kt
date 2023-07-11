package com.nicholas.rutherford.track.my.shot.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PendingUser(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "accountHasBeenCreated")
    val accountHasBeenCreated: Boolean,
    @ColumnInfo(name = "unverifiedEmail")
    val unverifiedEmail: String,
    @ColumnInfo(name = "unverifiedUsername")
    val unverifiedUsername: String
)

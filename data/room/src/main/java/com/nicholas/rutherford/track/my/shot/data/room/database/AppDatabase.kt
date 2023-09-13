package com.nicholas.rutherford.track.my.shot.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicholas.rutherford.track.my.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.my.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.my.shot.data.room.entities.UserEntity

@Database(entities = [ActiveUserEntity::class, UserEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activeUserDao(): ActiveUserDao

    abstract fun userDao(): UserDao
}

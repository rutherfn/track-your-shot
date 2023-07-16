package com.nicholas.rutherford.track.my.shot.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicholas.rutherford.track.my.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity

@Database(entities = [ActiveUserEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activeUserDao(): ActiveUserDao
}

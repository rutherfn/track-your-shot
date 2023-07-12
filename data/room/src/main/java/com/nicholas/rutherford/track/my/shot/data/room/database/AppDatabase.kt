package com.nicholas.rutherford.track.my.shot.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicholas.rutherford.track.my.shot.data.room.PendingUser
import com.nicholas.rutherford.track.my.shot.data.room.dao.PendingUserDao

@Database(entities = [PendingUser::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pendingUserDao(): PendingUserDao
}

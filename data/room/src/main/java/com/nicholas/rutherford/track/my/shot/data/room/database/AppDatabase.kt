package com.nicholas.rutherford.track.my.shot.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nicholas.rutherford.track.my.shot.data.room.PendingUser
import com.nicholas.rutherford.track.my.shot.data.room.dao.PendingUserDao

@Database(entities = [PendingUser::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pendingUserDao(): PendingUserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "app_database.db")
                .build()
        }
    }
}

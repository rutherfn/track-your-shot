package com.nicholas.rutherford.track.my.shot.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nicholas.rutherford.track.my.shot.data.room.converters.PlayerPositionsConverter
import com.nicholas.rutherford.track.my.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.my.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.my.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.my.shot.data.room.entities.PlayerEntity
import com.nicholas.rutherford.track.my.shot.data.room.entities.UserEntity

@Database(entities = [ActiveUserEntity::class, PlayerEntity::class, UserEntity::class], version = 4, exportSchema = false)
@TypeConverters(PlayerPositionsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activeUserDao(): ActiveUserDao

    abstract fun userDao(): UserDao
}

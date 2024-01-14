package com.nicholas.rutherford.track.your.shot.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nicholas.rutherford.track.your.shot.data.room.converters.PlayerPositionsConverter
import com.nicholas.rutherford.track.your.shot.data.room.converters.ShotLoggedConverter
import com.nicholas.rutherford.track.your.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.UserEntity

@Database(
    entities = [
        ActiveUserEntity::class,
        DeclaredShotEntity::class,
        PlayerEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(PlayerPositionsConverter::class, ShotLoggedConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activeUserDao(): ActiveUserDao

    abstract fun declaredShotDao(): DeclaredShotDao

    abstract fun playerDao(): PlayerDao

    abstract fun userDao(): UserDao
}

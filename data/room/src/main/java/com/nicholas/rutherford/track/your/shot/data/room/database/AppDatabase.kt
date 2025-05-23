package com.nicholas.rutherford.track.your.shot.data.room.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nicholas.rutherford.track.your.shot.data.room.converters.PlayerPositionsConverter
import com.nicholas.rutherford.track.your.shot.data.room.converters.ShotLoggedConverter
import com.nicholas.rutherford.track.your.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.DeclaredShotDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.IndividualPlayerReportDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.PendingPlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.PlayerDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.ShotIgnoringDao
import com.nicholas.rutherford.track.your.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.ActiveUserEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.IndividualPlayerReportEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PendingPlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.PlayerEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.ShotIgnoringEntity
import com.nicholas.rutherford.track.your.shot.data.room.entities.UserEntity

@Database(
    entities = [
        ActiveUserEntity::class,
        DeclaredShotEntity::class,
        IndividualPlayerReportEntity::class,
        PendingPlayerEntity::class,
        PlayerEntity::class,
        ShotIgnoringEntity::class,
        UserEntity::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11)

    ],
    version = 11,
    exportSchema = true
)
@TypeConverters(PlayerPositionsConverter::class, ShotLoggedConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activeUserDao(): ActiveUserDao

    abstract fun declaredShotDao(): DeclaredShotDao

    abstract fun individualPlayerReportDao(): IndividualPlayerReportDao

    abstract fun pendingPlayerDao(): PendingPlayerDao

    abstract fun playerDao(): PlayerDao

    abstract fun shotIgnoringDao(): ShotIgnoringDao

    abstract fun userDao(): UserDao
}

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

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Room database definition for the app. Includes all entities and DAOs.
 * Handles database migrations automatically through [AutoMigration].
 * Uses [TypeConverters] for complex types like PlayerPositions and ShotLogged.
 *
 * Provides access to the following DAOs:
 * - [ActiveUserDao]
 * - [DeclaredShotDao]
 * - [IndividualPlayerReportDao]
 * - [PendingPlayerDao]
 * - [PlayerDao]
 * - [ShotIgnoringDao]
 * - [UserDao]
 */
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

    /** Provides access to ActiveUser table operations. */
    abstract fun activeUserDao(): ActiveUserDao

    /** Provides access to DeclaredShot table operations. */
    abstract fun declaredShotDao(): DeclaredShotDao

    /** Provides access to IndividualPlayerReport table operations. */
    abstract fun individualPlayerReportDao(): IndividualPlayerReportDao

    /** Provides access to PendingPlayer table operations. */
    abstract fun pendingPlayerDao(): PendingPlayerDao

    /** Provides access to Player table operations. */
    abstract fun playerDao(): PlayerDao

    /** Provides access to ShotIgnoring table operations. */
    abstract fun shotIgnoringDao(): ShotIgnoringDao

    /** Provides access to User table operations. */
    abstract fun userDao(): UserDao
}

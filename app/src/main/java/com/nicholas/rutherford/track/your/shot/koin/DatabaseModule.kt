package com.nicholas.rutherford.track.your.shot.koin

import androidx.room.Room
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Koin module responsible for providing Room database and DAO dependencies.
 *
 * This module sets up a single instance of [AppDatabase] and provides
 * individual DAO instances for injection throughout the app.
 */
object DatabaseModule {

    /** Koin module containing database and DAO definitions. */
    val modules = module {

        /**
         * Provides a singleton instance of [AppDatabase] using Room's
         * database builder with the app context and predefined database name.
         */
        single {
            Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                Constants.APP_DATABASE_NAME
            ).build()
        }

        /** Provides the ActiveUserDao from the AppDatabase. */
        single { get<AppDatabase>().activeUserDao() }

        /** Provides the DeclaredShotDao from the AppDatabase. */
        single { get<AppDatabase>().declaredShotDao() }

        /** Provides the IndividualPlayerReportDao from the AppDatabase. */
        single { get<AppDatabase>().individualPlayerReportDao() }

        /** Provides the UserDao from the AppDatabase. */
        single { get<AppDatabase>().userDao() }

        /** Provides the PendingPlayerDao from the AppDatabase. */
        single { get<AppDatabase>().pendingPlayerDao() }

        /** Provides the PlayerDao from the AppDatabase. */
        single { get<AppDatabase>().playerDao() }

        /** Provides the ShotIgnoringDao from the AppDatabase. */
        single { get<AppDatabase>().shotIgnoringDao() }
    }
}

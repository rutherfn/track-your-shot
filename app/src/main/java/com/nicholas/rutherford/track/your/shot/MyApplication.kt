package com.nicholas.rutherford.track.your.shot

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.koin.DatabaseModule
import com.nicholas.rutherford.track.your.shot.koin.ExtensionLogicModule
import com.nicholas.rutherford.track.your.shot.koin.FirebaseModule
import com.nicholas.rutherford.track.your.shot.koin.NavigationModule
import com.nicholas.rutherford.track.your.shot.koin.RepositoryDataModule
import com.nicholas.rutherford.track.your.shot.koin.SharedPreferenceModule
import com.nicholas.rutherford.track.your.shot.koin.ViewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Custom [Application] class for the base app.
 *
 * Responsibilities include:
 * 1. Setting up Timber logging for debug builds.
 * 2. Initializing Koin dependency injection modules.
 * 3. Creating the app's notification channel for system notifications.
 */
open class MyApplication : Application() {

    /**
     * Called when the application is starting, before any activity, service, or receiver objects have been created.
     * Initializes Timber, Koin, and the notification channel.
     */
    override fun onCreate() {
        super.onCreate()

        // Initialize Timber logging for debug builds
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize Koin dependency injection
        startKoinOnCreate()

        // Create notification channel for app notifications
        createNotificationChannel()
    }

    /**
     * Creates a notification channel required for displaying notifications on Android O+.
     * Uses constants defined in [Constants] for channel ID and name.
     */
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    /**
     * Initializes Koin for dependency injection with all the app's modules.
     * This includes shared preferences, database, repositories, Firebase, extension logic, navigation, and ViewModels.
     */
    fun startKoinOnCreate() {
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    SharedPreferenceModule.modules,
                    DatabaseModule.modules,
                    RepositoryDataModule.modules,
                    FirebaseModule.modules,
                    ExtensionLogicModule.modules,
                    NavigationModule.modules,
                    ViewModelsModule.modules
                )
            )
        }
    }
}

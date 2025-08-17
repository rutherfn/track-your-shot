package com.nicholas.rutherford.track.your.shot.koin

import android.net.ConnectivityManager
import android.os.Build
import com.nicholas.rutherford.track.your.shot.AppBarFactory
import com.nicholas.rutherford.track.your.shot.AppBarFactoryImpl
import com.nicholas.rutherford.track.your.shot.BuildConfig
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotViewModelExt
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotViewModelExtImpl
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManagerImpl
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExt
import com.nicholas.rutherford.track.your.shot.helper.extensions.date.DateExtImpl
import com.nicholas.rutherford.track.your.shot.helper.file.generator.PdfGenerator
import com.nicholas.rutherford.track.your.shot.helper.file.generator.PdfGeneratorImpl
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import com.nicholas.rutherford.track.your.shot.helper.network.NetworkImpl
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.navigation.NavigatorImpl
import com.nicholas.rutherford.track.your.shot.notifications.Notifications
import com.nicholas.rutherford.track.your.shot.notifications.NotificationsImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Koin module for providing core application logic and helper dependencies.
 *
 * This module defines singletons for:
 * - App bar creation ([AppBarFactory])
 * - Network connectivity checks ([Network])
 * - Build type information ([BuildType])
 * - Navigation handling ([Navigator])
 * - Account management ([AccountManager])
 * - Notifications ([Notifications])
 * - PDF generation ([PdfGenerator])
 * - Date utilities ([DateExt])
 * - LogShot feature extensions ([LogShotViewModelExt])
 *
 * Dependencies are scoped as singletons and injected where needed.
 */
object ExtensionLogicModule {

    /** Default coroutine scope used for background operations across services. */
    private val defaultCoroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** Koin module definitions. */
    val modules = module {

        /** Provides an [AppBarFactory] implementation. */
        single<AppBarFactory> {
            AppBarFactoryImpl(application = androidApplication())
        }

        /** Provides a [Network] implementation for connectivity checking. */
        single<Network> {
            NetworkImpl(connectivityManager = androidApplication().getSystemService(ConnectivityManager::class.java))
        }

        /** Provides [BuildType] information based on the current SDK version and build type. */
        single<BuildType> {
            BuildTypeImpl(
                sdkValue = Build.VERSION.SDK_INT,
                buildTypeValue = BuildConfig.BUILD_TYPE
            )
        }

        /** Provides a [Navigator] implementation for in-app navigation. */
        single<Navigator> {
            NavigatorImpl()
        }

        /**
         * Provides an [AccountManager] for managing user accounts and related
         * repositories, Firebase interactions, and shared preferences.
         */
        single<AccountManager> {
            AccountManagerImpl(
                scope = defaultCoroutineScope,
                application = get(),
                navigator = get(),
                activeUserRepository = get(),
                declaredShotRepository = get(),
                playerRepository = get(),
                individualPlayerReportRepository = get(),
                pendingPlayerRepository = get(),
                shotIgnoringRepository = get(),
                userRepository = get(),
                readFirebaseUserInfo = get(),
                existingUserFirebase = get(),
                createSharedPreferences = get()
            )
        }

        /** Provides a [Notifications] implementation for managing app notifications. */
        single<Notifications> {
            NotificationsImpl(application = androidApplication())
        }

        /** Provides a [PdfGenerator] implementation for generating PDF files. */
        single<PdfGenerator> {
            PdfGeneratorImpl(application = androidApplication())
        }

        /** Provides a [DateExt] implementation for date-related utilities. */
        single<DateExt> {
            DateExtImpl()
        }

        /**
         * Provides a [LogShotViewModelExt] implementation for managing
         * LogShot feature extensions with coroutine scope.
         */
        single<LogShotViewModelExt> {
            LogShotViewModelExtImpl(
                application = androidApplication(),
                scope = defaultCoroutineScope
            )
        }
    }
}

package com.nicholas.rutherford.track.your.shot.koin

import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.IndividualPlayerReportRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.SavedVoiceCommandRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepositoryImpl
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.UserRepositoryImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShotImpl
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Koin module for providing repository layer dependencies.
 *
 * This module binds interfaces for all data repositories to their Room-based implementations
 * or in-memory objects, ensuring consistent access to persistent and current data.
 */
object RepositoryDataModule {

    /** Koin module definitions for repository instances. */
    val modules = module {

        /** Repository for active user data. */
        single<ActiveUserRepository> { ActiveUserRepositoryImpl(activeUserDao = get()) }

        /** Repository for declared shots. */
        single<DeclaredShotRepository> { DeclaredShotRepositoryImpl(declaredShotDao = get()) }

        /** Repository for individual player reports. */
        single<IndividualPlayerReportRepository> { IndividualPlayerReportRepositoryImpl(individualPlayerReportDao = get()) }

        /** In-memory object tracking the current pending shot. */
        single<CurrentPendingShot> { CurrentPendingShotImpl() }

        /** Repository for user data. */
        single<UserRepository> { UserRepositoryImpl(userDao = get()) }

        /** Repository for shots that should be ignored. */
        single<ShotIgnoringRepository> { ShotIgnoringRepositoryImpl(shotIgnoringDao = get()) }

        /** Repository for player data. */
        single<PlayerRepository> { PlayerRepositoryImpl(playerDao = get()) }

        /** Repository for pending players. */
        single<PendingPlayerRepository> { PendingPlayerRepositoryImpl(pendingPlayerDao = get()) }

        /** Repository for saved voice commands. */
        single<SavedVoiceCommandRepository> { SavedVoiceCommandRepositoryImpl(savedVoiceCommandDao = get()) }
    }
}

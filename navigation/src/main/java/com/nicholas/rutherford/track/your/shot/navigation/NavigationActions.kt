package com.nicholas.rutherford.track.your.shot.navigation

import androidx.navigation.NavOptions

object NavigationActions {

    object SplashScreen {
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        fun login() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, false)
                .build()
        }

        fun authentication(username: String, email: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.authenticationWithParams(username = username, email = email)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        fun termsConditions(isAcknowledgeConditions: Boolean) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.termsConditionsWithParams(isAcknowledgeConditions = isAcknowledgeConditions)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object LoginScreen {
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        fun createAccount() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_ACCOUNT_SCREEN
            override val navOptions = NavOptions.Builder()
                .build()
        }
        fun forgotPassword() = object : NavigationAction {
            override val destination = NavigationDestinations.FORGOT_PASSWORD_SCREEN
            override val navOptions = NavOptions.Builder()
                .build()
        }
    }

    object CreateAccountScreen {
        fun authentication(username: String, email: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.authenticationWithParams(username = username, email = email)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }
    }

    object AuthenticationScreen {

        fun login() = object : NavigationAction {
            override val destination: String = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        fun termsConditions(isAcknowledgeConditions: Boolean) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.termsConditionsWithParams(isAcknowledgeConditions = isAcknowledgeConditions)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object DrawerScreen {

        fun logout() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    object PlayersList {

        fun createEditPlayer() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }

        fun createEditPlayerWithParams(firstName: String, lastName: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.createEditPlayerWithParams(firstName = firstName, lastName = lastName)
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object ShotsList {

        fun logShot(
            isExistingPlayer: Boolean,
            playerId: Int,
            shotType: Int,
            shotId: Int,
            viewCurrentExistingShot: Boolean,
            viewCurrentPendingShot: Boolean,
            fromShotList: Boolean
        ) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.logShotWithParams(
                isExistingPlayer = isExistingPlayer,
                playerId = playerId,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = viewCurrentExistingShot,
                viewCurrentPendingShot = viewCurrentPendingShot,
                fromShotList = fromShotList
            )
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object CreateEditPlayer {

        fun selectShot(isExistingPlayer: Boolean, playerId: Int) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.selectShotWithParams(
                isExistingPlayer = isExistingPlayer,
                playerId = playerId
            )
            override val navOptions = NavOptions.Builder().build()
        }

        fun logShot(
            isExistingPlayer: Boolean,
            playerId: Int,
            shotType: Int,
            shotId: Int,
            viewCurrentExistingShot: Boolean,
            viewCurrentPendingShot: Boolean,
            fromShotList: Boolean
        ) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.logShotWithParams(
                isExistingPlayer = isExistingPlayer,
                playerId = playerId,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = viewCurrentExistingShot,
                viewCurrentPendingShot = viewCurrentPendingShot,
                fromShotList = fromShotList
            )
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object SelectShot {
        fun logShot(
            isExistingPlayer: Boolean,
            playerId: Int,
            shotType: Int,
            shotId: Int,
            viewCurrentExistingShot: Boolean,
            viewCurrentPendingShot: Boolean,
            fromShotList: Boolean
        ) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.logShotWithParams(
                isExistingPlayer = isExistingPlayer,
                playerId = playerId,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = viewCurrentExistingShot,
                viewCurrentPendingShot = viewCurrentPendingShot,
                fromShotList = fromShotList
            )
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object LogShot {

        fun createEditPlayer() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    object ReportList {

        fun createReport() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_REPORT_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    object Settings {

        fun accountInfo(username: String, email: String) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.accountInfoWithParams(
                username = username,
                email = email
            )
            override val navOptions = NavOptions.Builder().build()
        }

        fun enabledPermissions() = object : NavigationAction {
            override val destination = NavigationDestinations.ENABLED_PERMISSIONS_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        fun permissionEducation() = object : NavigationAction {
            override val destination = NavigationDestinations.PERMISSION_EDUCATION_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        fun onboardingEducation() = object : NavigationAction {
            override val destination = NavigationDestinations.ONBOARDING_EDUCATION_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        fun termsConditions(isAcknowledgeConditions: Boolean) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.termsConditionsWithParams(isAcknowledgeConditions = isAcknowledgeConditions)
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    object TermsConditions {
        fun onboardingEducation() = object : NavigationAction {
            override val destination = NavigationDestinations.ONBOARDING_EDUCATION_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }

        fun playerList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        fun settings() = object : NavigationAction {
            override val destination = NavigationDestinations.SETTINGS_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }
    }
}

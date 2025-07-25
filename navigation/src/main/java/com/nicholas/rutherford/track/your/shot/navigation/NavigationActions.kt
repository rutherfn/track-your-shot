package com.nicholas.rutherford.track.your.shot.navigation

import android.net.Uri
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

        fun authentication(username: String? = null, email: String? = null) =
            object : NavigationAction {
                override val destination: String = buildString {
                    append("authenticationScreen")

                    val queryParams = mutableListOf<String>()

                    username?.takeIf { it.isNotEmpty() }?.let {
                        queryParams += "username=${Uri.encode(it)}"
                    }

                    email?.takeIf { it.isNotEmpty() }?.let {
                        queryParams += "email=${Uri.encode(it)}"
                    }

                    if (queryParams.isNotEmpty()) {
                        append("?")
                        append(queryParams.joinToString("&"))
                    }
                }

                override val navOptions = NavOptions.Builder()
                    .setPopUpTo(0, true)
                    .build()
            }

        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("termsConditionsScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "shouldAcceptTerms=$shouldAcceptTerms"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
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

        /**
         * Object containing predefined navigation actions for the app.
         *
         * The `NavigationActions` object centralizes navigation logic and makes it easier to define
         * common navigation actions, such as navigating to the Authentication screen, by using
         * a specific function to construct the necessary destination and navigation options.
         */

        /**
         * Creates a navigation action to navigate to the "Authentication" screen.
         *
         * This function builds a [NavigationAction] object that navigates to the authentication screen
         * and optionally passes the `username` and `email` as query parameters.
         *
         * If either parameter is `null` or empty, it will be excluded from the query string.
         *
         * @param username Optional username to pre-fill on the authentication screen.
         * @param email Optional email to pre-fill on the authentication screen.
         * @return A [NavigationAction] object to use with a NavController for navigation.
         */
        fun authentication(username: String? = null, email: String? = null) =
            object : NavigationAction {
                override val destination: String = buildString {
                    append("authenticationScreen")

                    val queryParams = mutableListOf<String>()

                    username?.takeIf { it.isNotEmpty() }?.let {
                        queryParams += "username=${Uri.encode(it)}"
                    }

                    email?.takeIf { it.isNotEmpty() }?.let {
                        queryParams += "email=${Uri.encode(it)}"
                    }

                    if (queryParams.isNotEmpty()) {
                        append("?")
                        append(queryParams.joinToString("&"))
                    }
                }

                override val navOptions = NavOptions.Builder()
                    .setPopUpTo(0, true)
                    .build()
            }

        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("termsConditionsScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "shouldAcceptTerms=$shouldAcceptTerms"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
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

        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("termsConditionsScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "shouldAcceptTerms=$shouldAcceptTerms"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
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

        fun createEditPlayerWithParams(firstName: String?, lastName: String?) = object : NavigationAction {
            override val destination: String = buildString {
                append("createEditPlayerScreen")

                val queryParams = mutableListOf<String>()

                firstName?.let {
                    queryParams += "firstName=${Uri.encode(it)}"
                }

                lastName?.let {
                    queryParams += "lastName=${Uri.encode(it)}"
                }

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder().build()
        }

        fun shotList(shouldShowAllPlayersShots: Boolean) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.shotsListScreenWithParams(shouldShowAllPlayersShots = shouldShowAllPlayersShots)
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
            override val destination: String = buildString {
                append("logShotScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isExistingPlayer=$isExistingPlayer"

                queryParams += "playerId=$playerId"

                queryParams += "shotType=$shotType"

                queryParams += "shotId=$shotId"

                queryParams += "viewCurrentExistingShot=$viewCurrentExistingShot"

                queryParams += "viewCurrentPendingShot=$viewCurrentPendingShot"

                queryParams += "fromShotList=$fromShotList"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object CreateEditPlayer {

        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        fun selectShot(isExistingPlayer: Boolean, playerId: Int) = object : NavigationAction {

            override val destination: String = buildString {
                append("selectShotScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isExistingPlayer=$isExistingPlayer"

                queryParams += "playerId=$playerId"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }

//            override val destination = NavigationDestinationsWithParams.selectShotWithParams(
//                isExistingPlayer = isExistingPlayer,
//                playerId = playerId
//            )
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
            override val destination: String = buildString {
                append("logShotScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isExistingPlayer=$isExistingPlayer"

                queryParams += "playerId=$playerId"

                queryParams += "shotType=$shotType"

                queryParams += "shotId=$shotId"

                queryParams += "viewCurrentExistingShot=$viewCurrentExistingShot"

                queryParams += "viewCurrentPendingShot=$viewCurrentPendingShot"

                queryParams += "fromShotList=$fromShotList"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
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
            override val destination: String = buildString {
                append("logShotScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isExistingPlayer=$isExistingPlayer"

                queryParams += "playerId=$playerId"

                queryParams += "shotType=$shotType"

                queryParams += "shotId=$shotId"

                queryParams += "viewCurrentExistingShot=$viewCurrentExistingShot"

                queryParams += "viewCurrentPendingShot=$viewCurrentPendingShot"

                queryParams += "fromShotList=$fromShotList"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder().build()
        }
    }

    object LogShot {

        fun createEditPlayer(firstName: String?, lastName: String?) = object : NavigationAction {
            override val destination: String = buildString {
                append("createEditPlayerScreen")

                val queryParams = mutableListOf<String>()

                firstName?.takeIf { it.isNotEmpty() }?.let {
                    queryParams += "firstName=${Uri.encode(it)}"
                }

                lastName?.takeIf { it.isNotEmpty() }?.let {
                    queryParams += "lastName=${Uri.encode(it)}"
                }

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
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

    object DeclaredShotsList {

        fun createEditDeclaredShot() = object : NavigationAction {
            override val destination: String = NavigationDestinations.CREATE_EDIT_DECLARED_SHOTS_SCREEN
            override val navOptions = NavOptions.Builder().build()
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

        fun declaredShotsList() = object : NavigationAction {
            override val destination: String = NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }

        fun enabledPermissions() = object : NavigationAction {
            override val destination = NavigationDestinations.ENABLED_PERMISSIONS_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        fun permissionEducation(isFirstTimeLaunched: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("onboardingEducationScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isFirstTimeLaunched=$isFirstTimeLaunched"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        fun onboardingEducation(isFirstTimeLaunched: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("onboardingEducationScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isFirstTimeLaunched=$isFirstTimeLaunched"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("termsConditionsScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "shouldAcceptTerms=$shouldAcceptTerms"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    object TermsConditions {
        fun onboardingEducation(isFirstTimeLaunched: Boolean) = object : NavigationAction {
            override val destination: String = buildString {
                append("onboardingEducationScreen")

                val queryParams = mutableListOf<String>()

                queryParams += "isFirstTimeLaunched=$isFirstTimeLaunched"

                if (queryParams.isNotEmpty()) {
                    append("?")
                    append(queryParams.joinToString("&"))
                }
            }
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

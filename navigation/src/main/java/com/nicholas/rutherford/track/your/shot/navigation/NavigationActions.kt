package com.nicholas.rutherford.track.your.shot.navigation

import android.net.Uri
import androidx.navigation.NavOptions
import com.nicholas.rutherford.track.your.shot.helper.extensions.UriEncoder

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Centralized collection of predefined navigation actions for the application.
 *
 * The [NavigationActions] object organizes navigation logic into screen-specific nested objects.
 * Each nested object defines reusable navigation actions as functions that return a [NavigationAction],
 * encapsulating the destination route and [NavOptions] configuration.
 *
 * This structure improves consistency, reduces boilerplate, and makes navigation behavior easier to manage.
 */
object NavigationActions {

    /**
     * Navigation actions originating from the Splash screen.
     */
    object SplashScreen {
        /** Navigate to the players list screen, clearing the back stack. */
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        /** Navigate to the login screen, preserving the back stack. */
        fun login() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, false)
                .build()
        }

        /**
         * Navigate to the authentication screen with optional `username` and `email` query parameters.
         */
        fun authentication(username: String? = null, email: String? = null) =
            object : NavigationAction {
                override val destination: String =
                    NavigationDestinationsWithParams.buildAuthenticationDestination(username, email)

                override val navOptions = NavOptions.Builder()
                    .setPopUpTo(0, true)
                    .build()
            }

        /**
         * Navigate to the terms and conditions screen with a flag for whether the user must accept them.
         */
        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String =
                NavigationDestinationsWithParams.buildTermsConditionsDestination(shouldAcceptTerms)

            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    /**
     * Navigation actions originating from the Login screen.
     */
    object LoginScreen {
        /** Navigate to the players list screen, clearing the back stack. */
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        /** Navigate to the create account screen. */
        fun createAccount() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_ACCOUNT_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }

        /** Navigate to the forgot password screen. */
        fun forgotPassword() = object : NavigationAction {
            override val destination = NavigationDestinations.FORGOT_PASSWORD_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Create Account screen.
     */
    object CreateAccountScreen {

        /**
         * Navigate to the authentication screen with optional `username` and `email`.
         * Parameters are safely URL-encoded before being appended as query params.
         */
        fun authentication(username: String? = null, email: String? = null) =
            object : NavigationAction {
                override val destination: String = buildString {
                    append("authenticationScreen")

                    val queryParams = mutableListOf<String>()

                    username?.takeIf { it.isNotEmpty() }?.let {
                        queryParams += "username=${UriEncoder.encode(it)}"
                    }

                    email?.takeIf { it.isNotEmpty() }?.let {
                        queryParams += "email=${UriEncoder.encode(it)}"
                    }

                    if (queryParams.isNotEmpty()) {
                        append("?${queryParams.joinToString("&")}")
                    }
                }

                override val navOptions = NavOptions.Builder()
                    .setPopUpTo(0, true)
                    .build()
            }

        /** Navigate to the terms and conditions screen with a required acceptance flag. */
        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String =
                "termsConditionsScreen?shouldAcceptTerms=$shouldAcceptTerms"

            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    /**
     * Navigation actions originating from the Authentication screen.
     */
    object AuthenticationScreen {
        /** Navigate back to the login screen, clearing the back stack. */
        fun login() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        /** Navigate to the terms and conditions screen with a required acceptance flag. */
        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination: String =
                "termsConditionsScreen?shouldAcceptTerms=$shouldAcceptTerms"

            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    /**
     * Navigation actions available from the Drawer menu.
     */
    object DrawerScreen {
        /** Log out and navigate back to the login screen. */
        fun logout() = object : NavigationAction {
            override val destination = NavigationDestinations.LOGIN_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }

        /** Navigate to the players list screen. */
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .setLaunchSingleTop(true)
                .build()
        }
    }

    /**
     * Navigation actions originating from the Players List screen.
     */
    object PlayersList {
        /**
         * Navigate to the create/edit player screen with optional first and last name pre-filled.
         */
        fun createEditPlayerWithParams(firstName: String?, lastName: String?) = object : NavigationAction {
            override val destination: String = buildString {
                append("createEditPlayerScreen")

                val queryParams = mutableListOf<String>()
                firstName?.let { queryParams += "firstName=$it" }
                lastName?.let { queryParams += "lastName=$lastName" }

                if (queryParams.isNotEmpty()) {
                    append("?${queryParams.joinToString("&")}")
                }
            }
            override val navOptions = NavOptions.Builder().build()
        }

        /**
         * Navigate to the shots list screen, with a flag for whether to show all players' shots.
         */
        fun shotList(shouldShowAllPlayersShots: Boolean) = object : NavigationAction {
            override val destination =
                NavigationDestinationsWithParams.shotsListScreenWithParams(shouldShowAllPlayersShots)
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Shots List screen.
     */
    object ShotsList {
        /**
         * Navigate to the log shot screen with detailed parameters about the shot and player context.
         */
        fun logShot(
            isExistingPlayer: Boolean,
            playerId: Int,
            shotType: Int,
            shotId: Int,
            viewCurrentExistingShot: Boolean,
            viewCurrentPendingShot: Boolean,
            fromShotList: Boolean
        ) = object : NavigationAction {
            override val destination = NavigationDestinationsWithParams.buildLogShotDestination(
                isExistingPlayer,
                playerId,
                shotType,
                shotId,
                viewCurrentExistingShot,
                viewCurrentPendingShot,
                fromShotList
            )
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Create/Edit Player screen.
     */
    object CreateEditPlayer {
        /** Navigate back to the players list screen. */
        fun playersList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        /**
         * Navigate to the select shot screen for the given player.
         */
        fun selectShot(isExistingPlayer: Boolean, playerId: Int) = object : NavigationAction {
            override val destination: String = buildString {
                append("selectShotScreen?isExistingPlayer=$isExistingPlayer&playerId=$playerId")
            }
            override val navOptions = NavOptions.Builder().build()
        }

        /**
         * Navigate to the log shot screen with player and shot details.
         */
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
                append("?isExistingPlayer=$isExistingPlayer")
                append("&playerId=$playerId")
                append("&shotType=$shotType")
                append("&shotId=$shotId")
                append("&viewCurrentExistingShot=$viewCurrentExistingShot")
                append("&viewCurrentPendingShot=$viewCurrentPendingShot")
                append("&fromShotList=$fromShotList")
            }
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Select Shot screen.
     */
    object SelectShot {
        /**
         * Navigate to the log shot screen with player and shot details.
         */
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
                append("?isExistingPlayer=$isExistingPlayer")
                append("&playerId=$playerId")
                append("&shotType=$shotType")
                append("&shotId=$shotId")
                append("&viewCurrentExistingShot=$viewCurrentExistingShot")
                append("&viewCurrentPendingShot=$viewCurrentPendingShot")
                append("&fromShotList=$fromShotList")
            }
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Log Shot screen.
     */
    object LogShot {
        /** Navigate back to the shots list screen. */
        fun shotList(shouldShowAllPlayersShots: Boolean) = object : NavigationAction {
            override val destination =
                NavigationDestinationsWithParams.shotsListScreenWithParams(shouldShowAllPlayersShots)
            override val navOptions = NavOptions.Builder().build()
        }

        /**
         * Navigate to the create/edit player screen with optional pre-filled name.
         */
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
                    append("?${queryParams.joinToString("&")}")
                }
            }
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    /**
     * Navigation actions originating from the Report List screen.
     */
    object ReportList {
        /** Navigate to the create report screen. */
        fun createReport() = object : NavigationAction {
            override val destination = NavigationDestinations.CREATE_REPORT_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    /**
     * Navigation actions originating from the Create/Edit Declared Shot screen.
     */
    object CreateEditDeclaredShot {
        /** Navigate back to the declared shots list screen. */
        fun declaredShotList() = object : NavigationAction {
            override val destination = NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Declared Shots List screen.
     */
    object DeclaredShotsList {
        /**
         * Navigate to the create/edit declared shot screen for the given shot name.
         */
        fun createEditDeclaredShot(shotName: String) = object : NavigationAction {
            override val destination =
                NavigationDestinationsWithParams.buildCreateEditDeclaredShotDestination(shotName)
            override val navOptions = NavOptions.Builder().build()
        }
    }

    /**
     * Navigation actions originating from the Settings screen.
     */
    object Settings {
        /**
         * Navigate to the account info screen with username and email.
         */
        fun accountInfo(username: String, email: String) = object : NavigationAction {
            override val destination =
                NavigationDestinationsWithParams.accountInfoWithParams(username, email)
            override val navOptions = NavOptions.Builder().build()
        }

        /** Navigate to the declared shots list screen. */
        fun declaredShotsList() = object : NavigationAction {
            override val destination = NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN
            override val navOptions = NavOptions.Builder().build()
        }

        /** Navigate to the enabled permissions screen, clearing the back stack to players list. */
        fun enabledPermissions() = object : NavigationAction {
            override val destination = NavigationDestinations.ENABLED_PERMISSIONS_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        /** Navigate to the permission education screen. */
        fun permissionEducation() = object : NavigationAction {
            override val destination = NavigationDestinations.PERMISSION_EDUCATION_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        /**
         * Navigate to the onboarding education screen with a flag indicating if it’s the first launch.
         */
        fun onboardingEducation(isFirstTimeLaunched: Boolean) = object : NavigationAction {
            override val destination =
                "onboardingEducationScreen?isFirstTimeLaunched=$isFirstTimeLaunched"

            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }

        /** Navigate to the terms and conditions screen with an acceptance flag. */
        fun termsConditions(shouldAcceptTerms: Boolean) = object : NavigationAction {
            override val destination =
                "termsConditionsScreen?shouldAcceptTerms=$shouldAcceptTerms"

            override val navOptions = NavOptions.Builder()
                .setPopUpTo(NavigationDestinations.PLAYERS_LIST_SCREEN, true)
                .build()
        }
    }

    /**
     * Navigation actions originating from the Terms and Conditions screen.
     */
    object TermsConditions {
        /**
         * Navigate to the onboarding education screen with a flag indicating if it’s the first launch.
         */
        fun onboardingEducation(isFirstTimeLaunched: Boolean) = object : NavigationAction {
            override val destination =
                "onboardingEducationScreen?isFirstTimeLaunched=$isFirstTimeLaunched"

            override val navOptions = NavOptions.Builder().build()
        }

        /** Navigate to the players list screen, clearing the back stack. */
        fun playerList() = object : NavigationAction {
            override val destination = NavigationDestinations.PLAYERS_LIST_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }

        /** Navigate to the settings screen, clearing the back stack. */
        fun settings() = object : NavigationAction {
            override val destination = NavigationDestinations.SETTINGS_SCREEN
            override val navOptions = NavOptions.Builder()
                .setPopUpTo(0, true)
                .build()
        }
    }
}

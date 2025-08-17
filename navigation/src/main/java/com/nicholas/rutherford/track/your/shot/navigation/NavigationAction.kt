package com.nicholas.rutherford.track.your.shot.navigation

import androidx.navigation.NavOptions

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Represents a navigation action with a destination route and optional navigation options.
 *
 * Implementations of this interface define where to navigate to and how the navigation should behave.
 *
 * @property destination The navigation route (screen) to navigate to.
 * @property navOptions Optional [NavOptions] controlling the navigation animation, back stack behavior, etc.
 *                      Defaults to an empty [NavOptions] built via [NavOptions.Builder].
 */
interface NavigationAction {
    val destination: String
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
}

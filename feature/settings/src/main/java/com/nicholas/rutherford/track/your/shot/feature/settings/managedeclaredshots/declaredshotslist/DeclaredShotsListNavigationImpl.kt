package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [DeclaredShotsListNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class DeclaredShotsListNavigationImpl(private val navigator: Navigator) : DeclaredShotsListNavigation {
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun createEditDeclaredShot(shotName: String) = navigator.navigate(navigationAction = NavigationActions.DeclaredShotsList.createEditDeclaredShot(shotName = shotName))
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.SETTINGS_SCREEN)
}

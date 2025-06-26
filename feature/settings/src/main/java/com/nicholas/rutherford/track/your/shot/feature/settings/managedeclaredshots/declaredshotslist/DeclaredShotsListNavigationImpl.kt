package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class DeclaredShotsListNavigationImpl(private val navigator: Navigator) : DeclaredShotsListNavigation {
    override fun disableProgress() = navigator.progress(progressAction = null)
    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
    override fun createEditDeclaredShot() = navigator.navigate(navigationAction = NavigationActions.DeclaredShotsList.createEditDeclaredShot())
    override fun pop() = navigator.pop(popRouteAction = NavigationDestinations.SETTINGS_SCREEN)
}

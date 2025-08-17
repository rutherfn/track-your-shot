package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of [CreateEditDeclaredShotNavigation] using a [Navigator] to perform
 * navigation via predefined [NavigationActions].
 */
class CreateEditDeclaredShotNavigationImpl(private val navigator: Navigator) : CreateEditDeclaredShotNavigation {
    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)

    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)

    override fun navigateToDeclaredShotList() = navigator.navigate(navigationAction = NavigationActions.CreateEditDeclaredShot.declaredShotList())
}

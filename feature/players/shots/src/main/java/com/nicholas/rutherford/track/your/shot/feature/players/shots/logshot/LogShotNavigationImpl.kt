package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class LogShotNavigationImpl(private val navigator: Navigator) : LogShotNavigation {

    override fun alert(alert: Alert) = navigator.alert(alertAction = alert)
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)

    override fun popToShotList() = navigator.pop(popRouteAction = NavigationDestinations.SHOTS_LIST_SCREEN_WITH_PARAMS)

    override fun popToCreatePlayer() = navigator.pop(popRouteAction = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN)

    override fun popToEditPlayer() = navigator.pop(popRouteAction = NavigationDestinations.CREATE_EDIT_PLAYER_SCREEN_WITH_PARAMS)

    override fun datePicker(datePickerInfo: DatePickerInfo) = navigator.datePicker(datePickerAction = datePickerInfo)

    override fun inputInfo(inputInfo: InputInfo) = navigator.inputInfo(inputInfoAction = inputInfo)
    override fun disableProgress() = navigator.progress(progressAction = null)

    override fun enableProgress(progress: Progress) = navigator.progress(progressAction = progress)
}

package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.Navigator

class LogShotNavigationImpl(private val navigator: Navigator) : LogShotNavigation {
    override fun pop() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)

    override fun datePicker(datePickerInfo: DatePickerInfo) = navigator.datePicker(datePickerAction = datePickerInfo)
}

package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo

interface LogShotNavigation {
    fun pop()
    fun datePicker(datePickerInfo: DatePickerInfo)
    fun inputInfo(inputInfo: InputInfo)
}

package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface LogShotNavigation {
    fun alert(alert: Alert)
    fun pop()
    fun popToShotList()
    fun popToCreatePlayer()
    fun popToEditPlayer()
    fun datePicker(datePickerInfo: DatePickerInfo)
    fun inputInfo(inputInfo: InputInfo)
    fun disableProgress()
    fun enableProgress(progress: Progress)
}

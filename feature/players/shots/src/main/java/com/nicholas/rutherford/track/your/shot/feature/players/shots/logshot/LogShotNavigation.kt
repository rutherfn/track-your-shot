package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface LogShotNavigation {
    fun pop()
    fun datePicker(datePickerInfo: DatePickerInfo)
    fun inputInfo(inputInfo: InputInfo)
    fun disableProgress()
    fun enableProgress(progress: Progress)
}

package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

/**\
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines navigation actions available from the log shot screen.
 */
interface LogShotNavigation {
    fun alert(alert: Alert)
    fun pop()
    fun popToShotList(shouldShowAllPlayersShots: Boolean)
    fun navigateToShotList(firstName: String?, lastName: String?)
    fun popToCreatePlayer()
    fun popToEditPlayer()
    fun datePicker(datePickerInfo: DatePickerInfo)
    fun inputInfo(inputInfo: InputInfo)
    fun disableProgress()
    fun enableProgress(progress: Progress)
}

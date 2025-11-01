package com.nicholas.rutherford.track.your.shot.feature.voice.commands.createeditvoicecommand

import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress

interface CreateEditVoiceCommandNavigation {
    fun alert(alert: Alert)
    fun enableProgress(progress: Progress)
    fun disableProgress()
    fun pop()
}

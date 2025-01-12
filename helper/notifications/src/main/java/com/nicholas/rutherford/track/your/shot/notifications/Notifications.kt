package com.nicholas.rutherford.track.your.shot.notifications

import android.net.Uri

interface Notifications {
    fun buildPlayerReportNotification(uri: Uri, title: String, description: String)
}

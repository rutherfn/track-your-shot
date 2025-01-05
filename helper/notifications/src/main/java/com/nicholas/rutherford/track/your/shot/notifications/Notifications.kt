package com.nicholas.rutherford.track.your.shot.notifications

interface Notifications {
    fun buildPlayerReportNotification(title: String, description: String)
}
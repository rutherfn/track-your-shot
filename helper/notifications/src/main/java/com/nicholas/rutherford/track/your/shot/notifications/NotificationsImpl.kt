package com.nicholas.rutherford.track.your.shot.notifications

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import kotlin.random.Random

class NotificationsImpl(private val application: Application) : Notifications {

    private val manager = application.getSystemService(NotificationManager::class.java)

    private fun notifyManagerWithNewNotification(notification: Notification) {
        manager.notify(
            Random.nextInt(),
            notification
        )
    }

    override fun buildPlayerReportNotification(title: String, description: String) {
        val notification = NotificationCompat.Builder(application,Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_sports_basketball)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
            .build()

        notifyManagerWithNewNotification(notification = notification)
    }
}
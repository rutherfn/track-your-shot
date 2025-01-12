package com.nicholas.rutherford.track.your.shot.notifications

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
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

    override fun buildPlayerReportNotification(uri: Uri, title: String, description: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val pendingIntent = PendingIntent.getActivity(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(application, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_sports_basketball)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notifyManagerWithNewNotification(notification = notification)
    }
}

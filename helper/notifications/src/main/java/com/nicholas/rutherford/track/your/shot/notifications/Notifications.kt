package com.nicholas.rutherford.track.your.shot.notifications

import android.net.Uri

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Interface defining notification-related functionality for the app.
 *
 * Implementations of this interface are responsible for building and displaying
 * notifications, such as player report notifications.
 */
interface Notifications {

    /**
     * Builds and displays a notification for a player report.
     *
     * @param uri The URI of the report file to be opened when the notification is clicked.
     * @param title The title of the notification.
     * @param description The description text shown in the notification.
     */
    fun buildPlayerReportNotification(uri: Uri, title: String, description: String)
}

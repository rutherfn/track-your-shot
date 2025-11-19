package com.nicholas.rutherford.track.your.shot.helper.extensions.date

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines an interface for retrieving the current timestamp.
 */
interface DateExt {

    /**
     * Returns the current time in milliseconds.
     */
    val now: Long

    /**
     * Returns number of milliseconds in one minute.
     */
    val oneMinute: Long

    /**
     * Returns number of milliseconds in one day.
     */
    val oneDay: Long
}

package com.nicholas.rutherford.track.your.shot.helper.extensions.date

import java.util.Date

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Provides the current timestamp in milliseconds.
 */
class DateExtImpl : DateExt {

    /**
     * Returns the current time in milliseconds.
     */
    override val now: Long = Date().time
}

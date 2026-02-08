package com.nicholas.rutherford.track.your.shot.data.room.response

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the filter state for players with shots logged.
 * Uses a sealed class to define all possible filter states.
 *
 * @property value Integer value representing this filter state.
 */
sealed class HasShotsLoggedFilter(val value: Int) {

    /** Filter for players who have shots logged */
    data object HasShots : HasShotsLoggedFilter(value = Constants.HAS_SHOTS_LOGGED_VALUE)

    /** Filter for players who do not have shots logged */
    data object NoShots : HasShotsLoggedFilter(value = Constants.NO_SHOTS_LOGGED_VALUE)

    /** Filter for both players with shots logged and without shots logged (show all) */
    data object Both : HasShotsLoggedFilter(value = Constants.BOTH_SHOTS_LOGGED_VALUE)

    /** Represents no filter applied */
    data object None : HasShotsLoggedFilter(value = Constants.NO_SHOTS_LOGGED_FILTER_VALUE)

    companion object {
        /**
         * Converts an integer value to a corresponding [HasShotsLoggedFilter] instance.
         * Defaults to [None] if the value does not match any known filter state.
         *
         * @param value Integer representation of a filter state.
         * @return Corresponding [HasShotsLoggedFilter] instance.
         */
        fun fromValue(value: Int?): HasShotsLoggedFilter {
            return when (value) {
                Constants.HAS_SHOTS_LOGGED_VALUE -> HasShots
                Constants.NO_SHOTS_LOGGED_VALUE -> NoShots
                Constants.BOTH_SHOTS_LOGGED_VALUE -> Both
                else -> None
            }
        }
    }

    /**
     * Converts a [HasShotsLoggedFilter] instance to a localized string.
     *
     * @param application Application context for accessing string resources.
     * @return Localized string for the filter state.
     */
    fun toLocalizedString(application: Application): String {
        return when (this) {
            HasShots -> application.getString(StringsIds.hasShots)
            NoShots -> application.getString(StringsIds.noShots)
            Both -> application.getString(StringsIds.both)
            None -> ""
        }
    }
}

/**
 * Converts a localized string to its corresponding [HasShotsLoggedFilter] instance.
 * Defaults to [None] if the string does not match any known filter state.
 *
 * @param application Application context for accessing string resources.
 * @return Corresponding [HasShotsLoggedFilter] instance.
 */
fun String.toHasShotsLoggedFilter(application: Application): HasShotsLoggedFilter {
    return when (this) {
        application.getString(StringsIds.hasShots) -> HasShotsLoggedFilter.HasShots
        application.getString(StringsIds.noShots) -> HasShotsLoggedFilter.NoShots
        application.getString(StringsIds.both) -> HasShotsLoggedFilter.Both
        else -> HasShotsLoggedFilter.None
    }
}

package com.nicholas.rutherford.track.your.shot.data.room.converters

import androidx.room.TypeConverter
import com.nicholas.rutherford.track.your.shot.data.room.response.HasShotsLoggedFilter
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A Room TypeConverter for converting between [HasShotsLoggedFilter] sealed class instances
 * and their corresponding integer representations for database storage.
 */
class HasShotsLoggedFilterConverter {

    /**
     * Converts an integer value from the database into a corresponding [HasShotsLoggedFilter] instance.
     *
     * @param value The integer value representing a filter state.
     * @return The corresponding [HasShotsLoggedFilter] instance.
     */
    @TypeConverter
    fun fromValue(value: Int?): HasShotsLoggedFilter {
        return HasShotsLoggedFilter.fromValue(value)
    }

    /**
     * Converts a [HasShotsLoggedFilter] instance to its corresponding integer representation
     * for storing in the database.
     *
     * @param filter The [HasShotsLoggedFilter] instance to convert.
     * @return The integer value corresponding to the filter state, or null if None.
     */
    @TypeConverter
    fun toValue(filter: HasShotsLoggedFilter?): Int? {
        return when (filter) {
            is HasShotsLoggedFilter.HasShots -> Constants.HAS_SHOTS_LOGGED_VALUE
            is HasShotsLoggedFilter.NoShots -> Constants.NO_SHOTS_LOGGED_VALUE
            is HasShotsLoggedFilter.Both -> Constants.BOTH_SHOTS_LOGGED_VALUE
            is HasShotsLoggedFilter.None -> null
            null -> null
        }
    }
}



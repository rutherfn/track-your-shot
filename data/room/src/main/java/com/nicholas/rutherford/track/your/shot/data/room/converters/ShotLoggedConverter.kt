package com.nicholas.rutherford.track.your.shot.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A Room TypeConverter for converting between a List of [ShotLogged] objects
 * and its JSON string representation for database storage.
 */
class ShotLoggedConverter {

    /**
     * Converts a JSON string from the database into a List of [ShotLogged] objects.
     *
     * @param value The JSON string representing a list of [ShotLogged].
     * @return A List of [ShotLogged] objects.
     */
    @TypeConverter
    fun fromString(value: String): List<ShotLogged> {
        val listType = object : TypeToken<List<ShotLogged>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Converts a List of [ShotLogged] objects into a JSON string for storing in the database.
     *
     * @param value The List of [ShotLogged] to convert.
     * @return A JSON string representing the list of [ShotLogged].
     */
    @TypeConverter
    fun toString(value: List<ShotLogged>): String {
        return Gson().toJson(value)
    }
}

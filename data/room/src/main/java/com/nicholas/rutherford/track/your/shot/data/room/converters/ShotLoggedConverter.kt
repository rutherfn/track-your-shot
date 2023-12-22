package com.nicholas.rutherford.track.your.shot.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged

class ShotLoggedConverter {
    @TypeConverter
    fun fromString(value: String): List<ShotLogged> {
        val listType = object : TypeToken<List<ShotLogged>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(value: List<ShotLogged>): String {
        return Gson().toJson(value)
    }
}

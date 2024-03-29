package com.nicholas.rutherford.track.your.shot.data.room.converters

import androidx.room.TypeConverter
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

class PlayerPositionsConverter {
    @TypeConverter
    fun fromValue(value: Int): PlayerPositions {
        return when (value) {
            Constants.POINT_GUARD_VALUE -> PlayerPositions.PointGuard
            Constants.SHOOTING_GUARD_VALUE -> PlayerPositions.ShootingGuard
            Constants.SMALL_FORWARD_VALUE -> PlayerPositions.SmallForward
            Constants.POWER_FORWARD_VALUE -> PlayerPositions.PowerForward
            Constants.CENTER -> PlayerPositions.Center
            else -> PlayerPositions.None
        }
    }

    @TypeConverter
    fun toValue(playerPosition: PlayerPositions): Int {
        return when (playerPosition) {
            is PlayerPositions.PointGuard -> Constants.POINT_GUARD_VALUE
            is PlayerPositions.ShootingGuard -> Constants.SHOOTING_GUARD_VALUE
            is PlayerPositions.SmallForward -> Constants.SMALL_FORWARD_VALUE
            is PlayerPositions.PowerForward -> Constants.POWER_FORWARD_VALUE
            is PlayerPositions.Center -> Constants.CENTER
            is PlayerPositions.None -> Constants.POSITION_NA
        }
    }
}

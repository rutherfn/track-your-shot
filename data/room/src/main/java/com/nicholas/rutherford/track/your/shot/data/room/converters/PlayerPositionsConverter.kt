package com.nicholas.rutherford.track.your.shot.data.room.converters

import androidx.room.TypeConverter
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * A Room TypeConverter for converting between [PlayerPositions] sealed class instances
 * and their corresponding integer representations for database storage.
 */
class PlayerPositionsConverter {

    /**
     * Converts an integer value from the database into a corresponding [PlayerPositions] enum.
     *
     * @param value The integer value representing a player position.
     * @return The corresponding [PlayerPositions] enum.
     */
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

    /**
     * Converts a [PlayerPositions] enum to its corresponding integer representation
     * for storing in the database.
     *
     * @param playerPosition The [PlayerPositions] enum to convert.
     * @return The integer value corresponding to the player position.
     */
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

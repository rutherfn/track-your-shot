package com.nicholas.rutherford.track.your.shot.data.room.response

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the position of a basketball player on the court.
 * Uses a sealed class to define all possible positions including a default [None].
 *
 * @property value Integer value representing this player position.
 */
sealed class PlayerPositions(val value: Int) {

    /** Point Guard position */
    data object PointGuard : PlayerPositions(value = Constants.POINT_GUARD_VALUE)

    /** Shooting Guard position */
    data object ShootingGuard : PlayerPositions(value = Constants.SHOOTING_GUARD_VALUE)

    /** Small Forward position */
    data object SmallForward : PlayerPositions(value = Constants.SMALL_FORWARD_VALUE)

    /** Power Forward position */
    data object PowerForward : PlayerPositions(value = Constants.POWER_FORWARD_VALUE)

    /** Center position */
    data object Center : PlayerPositions(value = Constants.CENTER)

    /** Represents no specific position */
    data object None : PlayerPositions(value = Constants.POSITION_NA)

    companion object {
        /**
         * Converts an integer value to its corresponding [PlayerPositions] instance.
         * Defaults to [None] if the value does not match any known position.
         *
         * @param value Integer representation of a player position.
         * @return Corresponding [PlayerPositions] instance.
         */
        fun fromValue(value: Int): PlayerPositions {
            return when (value) {
                Constants.POINT_GUARD_VALUE -> PointGuard
                Constants.SHOOTING_GUARD_VALUE -> ShootingGuard
                Constants.SMALL_FORWARD_VALUE -> SmallForward
                Constants.POWER_FORWARD_VALUE -> PowerForward
                Constants.CENTER -> Center
                else -> None
            }
        }
    }

    /**
     * Converts a [PlayerPositions] instance to a localized string representing the position.
     *
     * @param application Application context for accessing string resources.
     * @return Localized string for the player position.
     */
    fun PlayerPositions.toPlayerPositionValue(application: Application): String {
        return when (this) {
            PointGuard -> application.getString(StringsIds.pointGuard)
            ShootingGuard -> application.getString(StringsIds.shootingGuard)
            SmallForward -> application.getString(StringsIds.smallForward)
            PowerForward -> application.getString(StringsIds.powerForward)
            else -> application.getString(StringsIds.center)
        }
    }

    /**
     * Converts a localized string to its corresponding [PlayerPositions] instance.
     * Defaults to [PointGuard] if the string does not match any known position.
     *
     * @param application Application context for accessing string resources.
     * @return Corresponding [PlayerPositions] instance.
     */
    fun String.toPlayerPosition(application: Application): PlayerPositions {
        return when (this) {
            application.getString(StringsIds.pointGuard) -> PointGuard
            application.getString(StringsIds.shootingGuard) -> ShootingGuard
            application.getString(StringsIds.smallForward) -> SmallForward
            application.getString(StringsIds.powerForward) -> PowerForward
            application.getString(StringsIds.center) -> Center
            else -> PointGuard
        }
    }
}

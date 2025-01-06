package com.nicholas.rutherford.track.your.shot.data.room.response

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

sealed class PlayerPositions(val value: Int) {
    data object PointGuard : PlayerPositions(value = Constants.POINT_GUARD_VALUE)
    data object ShootingGuard : PlayerPositions(value = Constants.SHOOTING_GUARD_VALUE)
    data object SmallForward : PlayerPositions(value = Constants.SMALL_FORWARD_VALUE)
    data object PowerForward : PlayerPositions(value = Constants.POWER_FORWARD_VALUE)
    data object Center : PlayerPositions(value = Constants.CENTER)

    data object None : PlayerPositions(value = Constants.POSITION_NA)

    companion object {
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

    fun PlayerPositions.toPlayerPositionValue(application: Application): String {
        return when (this) {
            PointGuard -> application.getString(StringsIds.pointGuard)
            ShootingGuard -> application.getString(StringsIds.shootingGuard)
            SmallForward -> application.getString(StringsIds.smallForward)
            PowerForward -> application.getString(StringsIds.powerForward)
            else -> application.getString(StringsIds.center)
        }
    }

    fun String.toPlayerPosition(application: Application): PlayerPositions {
        return when (this) {
            application.getString(StringsIds.pointGuard) -> {
                PointGuard
            }
            application.getString(StringsIds.shootingGuard) -> {
                ShootingGuard
            }
            application.getString(StringsIds.smallForward) -> {
                SmallForward
            }
            application.getString(StringsIds.powerForward) -> {
                PowerForward
            }
            application.getString(StringsIds.center) -> {
                Center
            }
            else -> {
                PointGuard
            }
        }
    }
}

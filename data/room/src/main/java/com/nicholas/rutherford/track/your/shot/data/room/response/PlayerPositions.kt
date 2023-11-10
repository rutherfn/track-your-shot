package com.nicholas.rutherford.track.your.shot.data.room.response

import com.nicholas.rutherford.track.your.shot.helper.constants.Constants

sealed class PlayerPositions(val value: Int) {
    object PointGuard : PlayerPositions(value = Constants.POINT_GUARD_VALUE)
    object ShootingGuard : PlayerPositions(value = Constants.SHOOTING_GUARD_VALUE)
    object SmallForward : PlayerPositions(value = Constants.SMALL_FORWARD_VALUE)
    object PowerForward : PlayerPositions(value = Constants.POWER_FORWARD_VALUE)
    object Center : PlayerPositions(value = Constants.CENTER)
}

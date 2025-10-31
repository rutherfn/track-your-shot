package com.nicholas.rutherford.track.your.shot.build.type

/**
 * Interface for build type configuration.
 * Provides information about the current build variant.
 */
interface BuildType {
    fun isDebug(): Boolean
    fun isStage(): Boolean
    fun isRelease(): Boolean
}

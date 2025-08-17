package com.nicholas.rutherford.track.your.shot.build.type

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Interface representing the type of the current app build and its SDK version.
 * Provides helper functions to check if the current build is debug, release, or stage.
 */
interface BuildType {

    /** The SDK version of the current build. */
    val sdk: Int

    /**
     * Returns true if the current build type is debug.
     */
    fun isDebug(): Boolean

    /**
     * Returns true if the current build type is release.
     */
    fun isRelease(): Boolean

    /**
     * Returns true if the current build type is stage.
     */
    fun isStage(): Boolean
}

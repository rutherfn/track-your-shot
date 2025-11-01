package com.nicholas.rutherford.track.your.shot.build.type

/**
 * Constants representing different build types for the app.
 */
const val DEBUG_VERSION_NAME = "debug"
const val RELEASE_VERSION_NAME = "release"
const val STAGE_VERSION_NAME = "stage"

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Implementation of the [BuildType] interface, providing information about
 * the current build configuration and SDK version.
 *
 * @property sdkValue The SDK version of the current build.
 * @property buildTypeValue The type of the build as a string (debug, release, or stage).
 */
class BuildTypeImpl(
    sdkValue: Int,
    private val buildTypeValue: String
) : BuildType {

    /** The SDK version of the current build. */
    override val sdk = sdkValue

    /**
     * Returns true if the current build type is debug.
     */
    override fun isDebug(): Boolean = buildTypeValue == DEBUG_VERSION_NAME

    /**
     * Returns true if the current build type is release.
     */
    override fun isRelease(): Boolean = buildTypeValue == RELEASE_VERSION_NAME

    /**
     * Returns true if the current build type is stage.
     */
    override fun isStage(): Boolean = buildTypeValue == STAGE_VERSION_NAME
}

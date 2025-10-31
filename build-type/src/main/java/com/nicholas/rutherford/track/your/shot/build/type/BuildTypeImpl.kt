package com.nicholas.rutherford.track.your.shot.build.type

/**
 * Implementation of BuildType interface.
 * Determines build type based on SDK version and build configuration.
 */
class BuildTypeImpl(
    private val sdkValue: Int,
    private val buildTypeValue: String
) : BuildType {

    override fun isDebug(): Boolean = buildTypeValue == "debug"

    override fun isStage(): Boolean = buildTypeValue == "stage"

    override fun isRelease(): Boolean = buildTypeValue == "release"
}

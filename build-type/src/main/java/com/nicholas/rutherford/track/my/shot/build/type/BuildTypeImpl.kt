package com.nicholas.rutherford.track.my.shot.build.type

class BuildTypeImpl(private val buildTypeValue: String): BuildType {

    const val DEBUG_VERSION_NAME = "debug"
    const val RELEASE_VERSION_NAME = "release"
    const val STAGE_VERSION_NAME = "stage"

    override fun isDebug(): Boolean = buildTypeValue == DEBUG_VERSION_NAME

    override fun isRelease(): Boolean = buildTypeValue == RELEASE_VERSION_NAME

    override fun isStage(): Boolean = buildTypeValue == STAGE_VERSION_NAME
}
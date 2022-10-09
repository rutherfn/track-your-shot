package com.nicholas.rutherford.track.my.shot.build.type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class BuildTypeImplTest {

    lateinit var buildTypeImpl: BuildTypeImpl

    internal val debugVersionName = "debug"
    internal val releaseVersionName = "release"
    internal val stageVersionName = "stage"

    @BeforeEach
    fun beforeEach() {
        buildTypeImpl = BuildTypeImpl(buildTypeValue = debugVersionName)
    }

    @Nested inner class Constants {

        @Test fun `debug version name should result in debug`() {
            Assertions.assertEquals(DEBUG_VERSION_NAME, debugVersionName)
        }

        @Test fun `release version name should result in release`() {
            Assertions.assertEquals(RELEASE_VERSION_NAME, releaseVersionName)
        }

        @Test fun `stage version name should result in stage`() {
            Assertions.assertEquals(STAGE_VERSION_NAME, stageVersionName)
        }
    }
}

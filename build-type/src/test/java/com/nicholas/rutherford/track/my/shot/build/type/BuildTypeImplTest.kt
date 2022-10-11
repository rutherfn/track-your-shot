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

    @Nested inner class IsDebug {

        @Test fun `isDebug should result in true when buildTypeValue is debug`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = debugVersionName)

            Assertions.assertTrue(buildTypeImpl.isDebug())
        }

        @Test fun `isDebug should result in false when buildTypeValue is stage`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = stageVersionName)

            Assertions.assertFalse(buildTypeImpl.isDebug())
        }

        @Test fun `isDebug should result in false when buildTypeValue is release`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = releaseVersionName)

            Assertions.assertFalse(buildTypeImpl.isDebug())
        }
    }

    @Nested inner class IsRelease {

        @Test fun `isRelease should result in true when buildTypeValue is release`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = releaseVersionName)

            Assertions.assertTrue(buildTypeImpl.isRelease())
        }

        @Test fun `isRelease should result in false when buildTypeValue is debug`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = debugVersionName)

            Assertions.assertFalse(buildTypeImpl.isRelease())
        }

        @Test fun `isRelease should result in false when buildTypeValue is stage`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = stageVersionName)

            Assertions.assertFalse(buildTypeImpl.isRelease())
        }
    }

    @Nested inner class IsStage {

        @Test fun `isStage should result in true when buildTypeValue is stage`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = stageVersionName)

            Assertions.assertTrue(buildTypeImpl.isStage())
        }

        @Test fun `isStage should result in false when buildTypeValue is debug`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = debugVersionName)

            Assertions.assertFalse(buildTypeImpl.isStage())
        }

        @Test fun `isStage should result in false when buildTypeValue is release`() {
            buildTypeImpl = BuildTypeImpl(buildTypeValue = releaseVersionName)

            Assertions.assertFalse(buildTypeImpl.isStage())
        }
    }
}

package com.nicholas.rutherford.track.my.shot.app.center

import android.app.Application
import com.nicholas.rutherford.track.my.shot.build.type.BuildTypeImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class AppCenterImplTest {

    lateinit var appCenterImpl: AppCenterImpl

    internal val debugAppCenterSecretValue = "f10e0fb2-e4b7-4d56-92db-08db35433dfa"
    internal val releaseAppCenterSecretValue = "babe5ef0-7d3f-470c-b396-ecd83744e35"
    internal val stageAppCenterSecretValue = "b5f5d03d-27a9-4186-874a-5c85e184f0bc"

    private val debugVersionName = "debug"
    private val releaseVersionName = "release"
    private val stageVersionName = "stage"

    internal val application = Application()

    private val buildTypeDebug = BuildTypeImpl(buildTypeValue = debugVersionName)
    private val buildTypeRelease = BuildTypeImpl(buildTypeValue = releaseVersionName)
    private val buildTypeStage = BuildTypeImpl(buildTypeValue = stageVersionName)

    @BeforeEach
    fun beforeEach() {
        appCenterImpl = AppCenterImpl(application = application, buildType = buildTypeDebug)
    }

    @Nested
    inner class Constants {

        @Test fun `debug app center secret name should result in secret`() {
            Assertions.assertEquals(DEBUG_APP_CENTER_SECRET, debugAppCenterSecretValue)
        }

        @Test fun `release app center secret name should result in secret`() {
            Assertions.assertEquals(RELEASE_APP_CENTER_SECRET, releaseAppCenterSecretValue)
        }

        @Test fun `stage app center secret name should result in secret`() {
            Assertions.assertEquals(STAGE_APP_CENTER_SECRET, stageAppCenterSecretValue)
        }
    }

    @Nested inner class GenerateAppSecret {

        @Test fun `when build type is debug should return debug app center secret`() {
            appCenterImpl = AppCenterImpl(application = application, buildType = buildTypeDebug)

            Assertions.assertEquals(appCenterImpl.generateAppCenterAppSecret(), DEBUG_APP_CENTER_SECRET)
        }

        @Test fun `when build type is stage should return stage app center secret`() {
            appCenterImpl = AppCenterImpl(application = application, buildType = buildTypeStage)

            Assertions.assertEquals(appCenterImpl.generateAppCenterAppSecret(), STAGE_APP_CENTER_SECRET)
        }

        @Test fun `when build type is release should return release app center secret`() {
            appCenterImpl = AppCenterImpl(application = application, buildType = buildTypeRelease)

            Assertions.assertEquals(appCenterImpl.generateAppCenterAppSecret(), RELEASE_APP_CENTER_SECRET)
        }
    }
}

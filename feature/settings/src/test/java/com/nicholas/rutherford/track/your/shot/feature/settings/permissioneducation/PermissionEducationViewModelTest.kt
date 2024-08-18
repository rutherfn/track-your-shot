package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import android.app.Application
import com.nicholas.rutherford.track.your.shot.build.type.BuildTypeImpl
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PermissionEducationViewModelTest {

    private lateinit var permissionEducationViewModel: PermissionEducationViewModel

    private var navigation = mockk<PermissionEducationNavigation>(relaxed = true)

    private val buildTypeValue = "debug"
    private var buildType = BuildTypeImpl(sdkValue = 22, buildTypeValue = buildTypeValue)

    private val application = mockk<Application>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.readMediaImagesPermission) } returns "Read Media Images Permission"
        every { application.getString(StringsIds.readExternalStoragePermission) } returns "Read External Storage Permission"
        every { application.getString(StringsIds.readMediaImagesPermissionExplanation) } returns "We ask for read media images permission so users can select an image from their gallery when creating or editing a player profile. This permission allows the app to access the images stored on the device, enabling users to choose and associate images with their player profiles. Without this permission, users would not be able to select images from their gallery for this feature."
        every { application.getString(StringsIds.readExternalStoragePermissionExplanation) } returns "We ask for read external storage permission so users can select an image from their gallery when creating or editing a player profile. This permission allows the app to access images stored on the device\'s external storage, enabling users to choose and associate images with their player profiles. Without this permission, users would not be able to select images from external storage for this feature."
        every { application.getString(StringsIds.next) } returns "Next"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.cameraPermission) } returns "Camera Permission"
        every { application.getString(StringsIds.cameraPermissionExplanation) } returns "We ask for camera permission so users can take photos with their phone\'s camera when creating or editing a player profile. This permission allows the app to access the device\'s camera hardware, enabling users to capture and associate images with their player profiles. Without this permission, the app would not be able to use the camera for this feature."
        every { application.getString(StringsIds.androidPermissionsUrl) } returns "https://developer.android.com/guide/topics/permissions/overview"

        permissionEducationViewModel = PermissionEducationViewModel(
            navigation = navigation,
            buildType = buildType,
            application = application
        )
    }

    @Nested
    inner class Init {

        @Test
        fun `when should ask for external storage permission and camera permission returns true should update state`() {
            permissionEducationViewModel = PermissionEducationViewModel(
                navigation = navigation,
                buildType = BuildTypeImpl(sdkValue = 11, buildTypeValue = buildTypeValue),
                application = application
            )

            Assertions.assertEquals(
                permissionEducationViewModel.permissionEducationMutableStateFlow.value,
                PermissionEducationState(
                    educationInfoList = listOf(
                        EducationInfo(
                            title = "Read External Storage Permission",
                            description = "We ask for read external storage permission so users can select an image from their gallery when creating or editing a player profile. This permission allows the app to access images stored on the device's external storage, enabling users to choose and associate images with their player profiles. Without this permission, users would not be able to select images from external storage for this feature.",
                            drawableResId = DrawablesIds.gallery,
                            buttonText = "Next",
                            moreInfoVisible = true
                        ),
                        EducationInfo(
                            title = "Camera Permission",
                            description = "We ask for camera permission so users can take photos with their phone's camera when creating or editing a player profile. This permission allows the app to access the device's camera hardware, enabling users to capture and associate images with their player profiles. Without this permission, the app would not be able to use the camera for this feature.",
                            drawableResId = DrawablesIds.camera,
                            buttonText = "Got It",
                            moreInfoVisible = true
                        )
                    )
                )
            )
        }

        @Test
        fun `when should ask for read media images and camera permission returns true should update state`() {
            permissionEducationViewModel = PermissionEducationViewModel(
                navigation = navigation,
                buildType = BuildTypeImpl(sdkValue = 34, buildTypeValue = buildTypeValue),
                application = application
            )

            Assertions.assertEquals(
                permissionEducationViewModel.permissionEducationMutableStateFlow.value,
                PermissionEducationState(
                    educationInfoList = listOf(
                        EducationInfo(
                            title = "Read Media Images Permission",
                            description = "We ask for read media images permission so users can select an image from their gallery when creating or editing a player profile. This permission allows the app to access the images stored on the device, enabling users to choose and associate images with their player profiles. Without this permission, users would not be able to select images from their gallery for this feature.",
                            drawableResId = DrawablesIds.gallery,
                            buttonText = "Next",
                            moreInfoVisible = true
                        ),
                        EducationInfo(
                            title = "Camera Permission",
                            description = "We ask for camera permission so users can take photos with their phone's camera when creating or editing a player profile. This permission allows the app to access the device's camera hardware, enabling users to capture and associate images with their player profiles. Without this permission, the app would not be able to use the camera for this feature.",
                            drawableResId = DrawablesIds.camera,
                            buttonText = "Got It",
                            moreInfoVisible = true
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `on got it button clicked`() {
        permissionEducationViewModel.onGotItButtonClicked()

        verify { navigation.pop() }
    }

    @Test
    fun `on more info clicked`() {
        permissionEducationViewModel.onMoreInfoClicked()

        verify { navigation.navigateToUrl(url = "https://developer.android.com/guide/topics/permissions/overview") }
    }
}

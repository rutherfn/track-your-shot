package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EnabledPermissionsViewModelTest {

    private lateinit var enabledPermissionsViewModel: EnabledPermissionsViewModel

    private var navigation = mockk<EnabledPermissionsNavigation>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    private fun mockStrings() {
        every { application.getString(StringsIds.center) } returns "Center"
        every { application.getString(StringsIds.theImageUploadWasUnsuccessful) } returns "The image upload was unsuccessful. Please attempt it once more."
        every { application.getString(StringsIds.ok) } returns "Ok"
        every { application.getString(StringsIds.unableToUploadImage) } returns "Unable to upload image"
        every { application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription) } returns "We have detected currently not connected to internet. Please connect to service, and try again."
        every { application.getString(StringsIds.notConnectedToInternet) } returns "Not connected to internet"
        every { application.getString(StringsIds.noFirstNameEntered) } returns "No First Name Entered"
        every { application.getString(StringsIds.playersFirstNameEmptyDescription) } returns "The player\\'s first name is missing. Kindly provide a first name to proceed."
        every { application.getString(StringsIds.readMediaImagesDescription) } returns "Permission to read external storage has been declined. To manually enable the \"Read External Storage\" permission and select an image from the gallery for the Player, please navigate to settings."
        every { application.getString(StringsIds.readExternalStorageDescription) } returns "Permission to read external storage has been declined. To manually enable the \"Read External Storage\" permission and select an image from the gallery for the Player, please navigate to settings."
        every { application.getString(StringsIds.permissionHasBeenDeclined) } returns "Permission has been declined"
        every { application.getString(StringsIds.notNow) } returns "Not Now"
        every { application.getString(StringsIds.cameraPermissionHasBeenDeniedDescription) } returns "Camera permission has been denied. To manually grant permission for the camera and upload pictures for the Player, kindly navigate to Settings."
        every { application.getString(StringsIds.pg) } returns "Pg"
        every { application.getString(StringsIds.chooseOption) } returns "Choose Option"
        every { application.getString(StringsIds.chooseImageFromGallery) } returns "Choose Image From Gallery"
        every { application.getString(StringsIds.takeAPicture) } returns "Take A Picture"
        every { application.getString(StringsIds.removeImage) } returns "Remove Image"
        every { application.getString(StringsIds.settings) } returns "Settings"
        every { application.getString(StringsIds.notNow) } returns "Not Now"
        every { application.getString(StringsIds.cameraPermissionHasBeenDeniedDescription) } returns "Camera permission has been denied. To manually grant permission for the camera and upload pictures for the Player, kindly navigate to Settings."
        every { application.getString(StringsIds.unsavedPlayerChanges) } returns "Unsaved Player Changes"
        every { application.getString(StringsIds.doYouWishToProceedDescription) } returns "Do you wish to proceed despite having unsaved player modifications? Any changes made will not be saved."
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.no) } returns "No"
    }

    @BeforeEach
    fun beforeEach() {
        mockStrings()

        enabledPermissionsViewModel = EnabledPermissionsViewModel(
            navigation = navigation,
            application = application
        )
    }

    @Test
    fun `on toolbar menu clicked should navigate to pop`() {
        enabledPermissionsViewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }

    @Test
    fun `on switch changed to turn off permission should navigate to app settinghs`() {
        enabledPermissionsViewModel.onSwitchChangedToTurnOffPermission()

        verify { navigation.appSettings() }
    }

    @Test
    fun `camera permission not granted alert`() {
        val alert = enabledPermissionsViewModel.cameraPermissionNotGrantedAlert()

        Assertions.assertEquals(alert.title, "Permission has been declined")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Settings")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Not Now")
        Assertions.assertEquals(alert.description, "Camera permission has been denied. To manually grant permission for the camera and upload pictures for the Player, kindly navigate to Settings.")
    }

    @Test
    fun `permission not granted for camera alert should call alert for camera alert`() {
        enabledPermissionsViewModel.permissionNotGrantedForCameraAlert()

        verify { navigation.alert(alert = any()) }
    }
}

package com.nicholas.rutherford.track.your.shot.players.createeditplayer.helper

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.data.test.room.TestShotLogged
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerState
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper.CreateEditPlayerSharedHelper
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateEditPlayerSharedHelperTest {

    private lateinit var sharedHelper: CreateEditPlayerSharedHelper
    private val application = mockk<Application>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        mockStrings()
        sharedHelper = CreateEditPlayerSharedHelper(application = application)
    }

    private fun mockStrings() {
        every { application.getString(StringsIds.hintLogNewShots) } returns "Press the \"Log Shots\" button to record shots for the player."
        every { application.getString(StringsIds.hintLogNewShotsForPlayer) } returns "Press the \"Log Shots\" button to record shots for"
        every { application.getString(StringsIds.shotUpdated) } returns "Shot Updated"
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.currentShotHasBeenUpdatedDescription) } returns "The current shot has been updated."
        every { application.getString(StringsIds.permissionHasBeenDeclined) } returns "Permission has been declined"
        every { application.getString(StringsIds.settings) } returns "Settings"
        every { application.getString(StringsIds.notNow) } returns "Not Now"
        every { application.getString(StringsIds.cameraPermissionHasBeenDeniedDescription) } returns "Camera permission has been denied."
        every { application.getString(StringsIds.readExternalStorageDescription) } returns "Read external storage description"
        every { application.getString(StringsIds.readMediaImagesDescription) } returns "Read media images description"
        every { application.getString(StringsIds.noFirstNameEntered) } returns "No First Name Entered"
        every { application.getString(StringsIds.playersFirstNameEmptyDescription) } returns "The player's first name is missing."
        every { application.getString(StringsIds.notConnectedToInternet) } returns "Not connected to internet"
        every { application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription) } returns "We have detected currently not connected to internet."
        every { application.getString(StringsIds.unableToUploadImage) } returns "Unable to upload image"
        every { application.getString(StringsIds.theImageUploadWasUnsuccessful) } returns "The image upload was unsuccessful."
        every { application.getString(StringsIds.ok) } returns "Ok"
        every { application.getString(StringsIds.issueOccurred) } returns "Issue Occurred"
        every { application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue) } returns "We have detected a problem with your account."
        every { application.getString(StringsIds.playerAlreadyHasBeenAddedDescription) } returns "The player has already been added."
        every { application.getString(StringsIds.unsavedPlayerChanges) } returns "Unsaved Player Changes"
        every { application.getString(StringsIds.doYouWishToProceedDescription) } returns "Do you wish to proceed?"
        every { application.getString(StringsIds.yes) } returns "Yes"
        every { application.getString(StringsIds.no) } returns "No"
        every { application.getString(StringsIds.chooseOption) } returns "Choose Option"
        every { application.getString(StringsIds.removeImage) } returns "Remove Image"
        every { application.getString(StringsIds.chooseImageFromGallery) } returns "Choose Image From Gallery"
        every { application.getString(StringsIds.takeAPicture) } returns "Take A Picture"
    }

    @Nested
    inner class HintLogNewShotText {
        @Test
        fun `when firstName and lastName are both provided and not empty should return hint with both names`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = "John",
                lastName = "Doe",
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for John Doe",
                result
            )
        }

        @Test
        fun `when only firstName is provided and not empty should return hint with first name only`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = "John",
                lastName = "",
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for John",
                result
            )
        }

        @Test
        fun `when only lastName is provided and not empty should return hint with last name only`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = "",
                lastName = "Doe",
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for Doe",
                result
            )
        }

        @Test
        fun `when both firstName and lastName are empty should return default hint`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = "",
                lastName = "",
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for the player.",
                result
            )
        }

        @Test
        fun `when firstName is null should return default hint`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = null,
                lastName = "Doe",
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for the player.",
                result
            )
        }

        @Test
        fun `when lastName is null should return default hint`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = "John",
                lastName = null,
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for the player.",
                result
            )
        }

        @Test
        fun `when both firstName and lastName are null should return default hint`() {
            val result = sharedHelper.hintLogNewShotText(
                firstName = null,
                lastName = null,
                currentHintText = ""
            )

            Assertions.assertEquals(
                "Press the \"Log Shots\" button to record shots for the player.",
                result
            )
        }
    }

    @Nested
    inner class ValidatePlayer {
        @Test
        fun `when firstName is not empty should return true`() {
            val state = CreateEditPlayerState(firstName = "John")
            val result = sharedHelper.validatePlayer(state = state)

            Assertions.assertTrue(result)
        }

        @Test
        fun `when firstName is empty should return false`() {
            val state = CreateEditPlayerState(firstName = "")
            val result = sharedHelper.validatePlayer(state = state)

            Assertions.assertFalse(result)
        }
    }

    @Nested
    inner class CurrentShotsNotPending {
        @Test
        fun `when editedPlayerShots is empty should return empty list`() {
            val result = sharedHelper.currentShotsNotPending(
                editedPlayerShots = emptyList(),
                pendingShotLoggedList = emptyList()
            )

            Assertions.assertTrue(result.isEmpty())
        }

        @Test
        fun `when pendingShotLoggedList contains shot with same id should exclude it`() {
            val shot1 = TestShotLogged.build(id = 1)
            val shot2 = TestShotLogged.build(id = 2)
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = shot1,
                isPendingPlayer = false
            )

            val result = sharedHelper.currentShotsNotPending(
                editedPlayerShots = listOf(shot1, shot2),
                pendingShotLoggedList = listOf(pendingShot)
            )

            Assertions.assertEquals(1, result.size)
            Assertions.assertEquals(shot2.id, result.first().id)
        }

        @Test
        fun `when pendingShotLoggedList is empty should return all editedPlayerShots`() {
            val shot1 = TestShotLogged.build(id = 1)
            val shot2 = TestShotLogged.build(id = 2)

            val result = sharedHelper.currentShotsNotPending(
                editedPlayerShots = listOf(shot1, shot2),
                pendingShotLoggedList = emptyList()
            )

            Assertions.assertEquals(2, result.size)
        }
    }

    @Nested
    inner class CurrentShotLoggedRealtimeResponseList {
        @Test
        fun `when pendingShotLoggedList is empty should return currentShotList as is`() {
            val currentShot = ShotLoggedRealtimeResponse(id = 1)
            val currentShotList = listOf(currentShot)

            val result = sharedHelper.currentShotLoggedRealtimeResponseList(
                currentShotList = currentShotList,
                pendingShotLoggedList = emptyList()
            )

            Assertions.assertEquals(1, result.size)
            Assertions.assertEquals(currentShot.id, result.first().id)
        }

        @Test
        fun `when pendingShotLoggedList is not empty should combine and mark pending as false`() {
            val currentShot = ShotLoggedRealtimeResponse(id = 1)
            val currentShotList = listOf(currentShot)
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(id = 2),
                isPendingPlayer = false
            )

            val result = sharedHelper.currentShotLoggedRealtimeResponseList(
                currentShotList = currentShotList,
                pendingShotLoggedList = listOf(pendingShot)
            )

            Assertions.assertEquals(2, result.size)
            Assertions.assertFalse(result.last().isPending)
        }
    }

    @Nested
    inner class CurrentShotLoggedList {
        @Test
        fun `when pendingShotLoggedList is empty should return currentShotLoggedList as is`() {
            val currentShot = TestShotLogged.build(id = 1)
            val currentShotList = listOf(currentShot)

            val result = sharedHelper.currentShotLoggedList(
                currentShotLoggedList = currentShotList,
                pendingShotLoggedList = emptyList()
            )

            Assertions.assertEquals(1, result.size)
            Assertions.assertEquals(currentShot.id, result.first().id)
        }

        @Test
        fun `when pendingShotLoggedList is not empty should combine and mark pending as false`() {
            val currentShot = TestShotLogged.build(id = 1)
            val currentShotList = listOf(currentShot)
            val pendingShot = PendingShot(
                player = TestPlayer().create(),
                shotLogged = TestShotLogged.build(id = 2, isPending = true),
                isPendingPlayer = false
            )

            val result = sharedHelper.currentShotLoggedList(
                currentShotLoggedList = currentShotList,
                pendingShotLoggedList = listOf(pendingShot)
            )

            Assertions.assertEquals(2, result.size)
            Assertions.assertFalse(result.last().isPending)
        }
    }

    @Nested
    inner class ShotLoggedToRealtimeResponse {
        @Test
        fun `should convert ShotLogged to ShotLoggedRealtimeResponse correctly`() {
            val shotLogged = TestShotLogged.build()

            val result = sharedHelper.shotLoggedToRealtimeResponse(shotLogged = shotLogged)

            Assertions.assertEquals(shotLogged.id, result.id)
            Assertions.assertEquals(shotLogged.shotName, result.shotName)
            Assertions.assertEquals(shotLogged.shotType, result.shotType)
            Assertions.assertEquals(shotLogged.shotsAttempted, result.shotsAttempted)
            Assertions.assertEquals(shotLogged.shotsMade, result.shotsMade)
            Assertions.assertEquals(shotLogged.shotsMissed, result.shotsMissed)
            Assertions.assertEquals(shotLogged.isPending, result.isPending)
        }
    }

    @Nested
    inner class ShowUpdatedAlert {
        @Test
        fun `should return alert with correct title and description`() {
            val alert = sharedHelper.showUpdatedAlert()

            Assertions.assertEquals("Shot Updated", alert.title)
            Assertions.assertEquals("The current shot has been updated.", alert.description)
            Assertions.assertNotNull(alert.dismissButton)
            Assertions.assertEquals("Got It", alert.dismissButton?.buttonText)
        }
    }

    @Nested
    inner class CameraPermissionNotGrantedAlert {
        @Test
        fun `should return alert with correct properties and callback`() {
            var callbackInvoked = false
            val alert = sharedHelper.cameraPermissionNotGrantedAlert {
                callbackInvoked = true
            }

            Assertions.assertEquals("Permission has been declined", alert.title)
            Assertions.assertEquals("Camera permission has been denied.", alert.description)
            Assertions.assertNotNull(alert.confirmButton)
            Assertions.assertEquals("Settings", alert.confirmButton?.buttonText)
            alert.confirmButton?.onButtonClicked?.invoke()
            Assertions.assertTrue(callbackInvoked)
        }
    }

    @Nested
    inner class MediaOrExternalStorageNotGrantedAlert {
        @Test
        fun `when shouldAskForPermission is false should return alert with readExternalStorageDescription`() {
            var callbackInvoked = false
            val alert = sharedHelper.mediaOrExternalStorageNotGrantedAlert(
                shouldAskForPermission = false
            ) {
                callbackInvoked = true
            }

            Assertions.assertEquals("Read external storage description", alert.description)
        }

        @Test
        fun `when shouldAskForPermission is true should return alert with readMediaImagesDescription`() {
            var callbackInvoked = false
            val alert = sharedHelper.mediaOrExternalStorageNotGrantedAlert(
                shouldAskForPermission = true
            ) {
                callbackInvoked = true
            }

            Assertions.assertEquals("Read media images description", alert.description)
        }
    }

    @Nested
    inner class FirstNameEmptyAlert {
        @Test
        fun `should return alert with correct properties`() {
            val alert = sharedHelper.firstNameEmptyAlert()

            Assertions.assertEquals("No First Name Entered", alert.title)
            Assertions.assertEquals("The player's first name is missing.", alert.description)
            Assertions.assertNotNull(alert.dismissButton)
            Assertions.assertEquals("Got It", alert.dismissButton?.buttonText)
        }
    }

    @Nested
    inner class NotConnectedToInternetAlert {
        @Test
        fun `should return alert with correct properties`() {
            val alert = sharedHelper.notConnectedToInternetAlert()

            Assertions.assertEquals("Not connected to internet", alert.title)
            Assertions.assertEquals("We have detected currently not connected to internet.", alert.description)
            Assertions.assertNotNull(alert.dismissButton)
        }
    }

    @Nested
    inner class NotAbleToUploadImageAlert {
        @Test
        fun `should return alert with correct properties`() {
            val alert = sharedHelper.notAbleToUploadImageAlert()

            Assertions.assertEquals("Unable to upload image", alert.title)
            Assertions.assertEquals("The image upload was unsuccessful.", alert.description)
            Assertions.assertNotNull(alert.dismissButton)
            Assertions.assertEquals("Ok", alert.dismissButton?.buttonText)
        }
    }

    @Nested
    inner class WeHaveDetectedAProblemWithYourAccountAlert {
        @Test
        fun `should return alert with correct properties`() {
            val alert = sharedHelper.weHaveDetectedAProblemWithYourAccountAlert()

            Assertions.assertEquals("Issue Occurred", alert.title)
            Assertions.assertEquals("We have detected a problem with your account.", alert.description)
            Assertions.assertNotNull(alert.dismissButton)
        }
    }

    @Nested
    inner class PlayerAlreadyHasBeenAddedAlert {
        @Test
        fun `should return alert with correct properties`() {
            val alert = sharedHelper.playerAlreadyHasBeenAddedAlert()

            Assertions.assertEquals("Issue Occurred", alert.title)
            Assertions.assertEquals("The player has already been added.", alert.description)
            Assertions.assertNotNull(alert.dismissButton)
        }
    }

    @Nested
    inner class UnsavedPlayerChangesAlert {
        @Test
        fun `should return alert with correct properties and callback`() {
            var callbackInvoked = false
            val alert = sharedHelper.unsavedPlayerChangesAlert {
                callbackInvoked = true
            }

            Assertions.assertEquals("Unsaved Player Changes", alert.title)
            Assertions.assertEquals("Do you wish to proceed?", alert.description)
            Assertions.assertNotNull(alert.confirmButton)
            Assertions.assertEquals("Yes", alert.confirmButton?.buttonText)
            Assertions.assertNotNull(alert.dismissButton)
            Assertions.assertEquals("No", alert.dismissButton?.buttonText)
            alert.confirmButton?.onButtonClicked?.invoke()
            Assertions.assertTrue(callbackInvoked)
        }
    }

    @Nested
    inner class RemoveImageSheet {
        @Test
        fun `should return sheet with correct properties`() {
            val sheet = sharedHelper.removeImageSheet()

            Assertions.assertEquals("Choose Option", sheet.title)
            Assertions.assertEquals(1, sheet.values.size)
            Assertions.assertEquals("Remove Image", sheet.values.first())
        }
    }

    @Nested
    inner class ChooseFromGalleryOrTakePictureSheet {
        @Test
        fun `should return sheet with correct properties`() {
            val sheet = sharedHelper.chooseFromGalleryOrTakePictureSheet()

            Assertions.assertEquals("Choose Option", sheet.title)
            Assertions.assertEquals(2, sheet.values.size)
            Assertions.assertTrue(sheet.values.contains("Choose Image From Gallery"))
            Assertions.assertTrue(sheet.values.contains("Take A Picture"))
        }
    }
}


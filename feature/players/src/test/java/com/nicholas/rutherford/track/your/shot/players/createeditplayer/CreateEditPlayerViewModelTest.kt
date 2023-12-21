package com.nicholas.rutherford.track.your.shot.players.createeditplayer

import android.app.Application
import android.net.Uri
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.Center.toPlayerPosition
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUser
import com.nicholas.rutherford.track.your.shot.data.test.room.TestPlayer
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditImageOption
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerState
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PLAYER_FIREBASE_KEY
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateEditPlayerViewModelTest {

    private lateinit var createEditPlayerViewModel: CreateEditPlayerViewModel

    private val application = mockk<Application>(relaxed = true)

    private val createFirebaseUserInfo = mockk<CreateFirebaseUserInfo>(relaxed = true)
    private val updateFirebaseUserInfo = mockk<UpdateFirebaseUserInfo>(relaxed = true)
    private val readFirebaseUserInfo = mockk<ReadFirebaseUserInfo>(relaxed = true)

    private val playerRepository = mockk<PlayerRepository>(relaxed = true)
    private val activeUserRepository = mockk<ActiveUserRepository>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + testDispatcher)

    private val navigation = mockk<CreateEditPlayerNavigation>(relaxed = true)

    private val playersAdditionUpdates = mockk<PlayersAdditionUpdates>(relaxed = true)

    private val network = mockk<Network>(relaxed = true)

    private val uri = mockk<Uri>(relaxed = true)

    private val defaultState = CreateEditPlayerState(
        firstName = "first",
        lastName = "last",
        toolbarNameResId = StringsIds.createPlayer,
        playerPositionString = "",
        sheet = null
    )

    private fun mockStrings() {
        every { application.getString(StringsIds.center) } returns "Center"
        every { application.getString(StringsIds.theImageUploadWasUnsuccessful) } returns "The image upload was unsuccessful. Please attempt it once more."
        every { application.getString(StringsIds.ok) } returns "Ok"
        every { application.getString(StringsIds.unableToUploadImage) } returns "Unable to upload image"
        every { application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription) } returns "We have detected currently not connected to internet. Please connect to service, and try again."
        every { application.getString(StringsIds.notConnectedToInternet) } returns "Not connected to internet"
        every { application.getString(StringsIds.noFirstNameEntered) } returns "No First Name Entered"
        every { application.getString(StringsIds.noLastNameEntered) } returns "No Last Name Entered"
        every { application.getString(StringsIds.playersFirstNameEmptyDescription) } returns "The player\\'s first name is missing. Kindly provide a first name to proceed."
        every { application.getString(StringsIds.playersLastNameEmptyDescription) } returns "The player\\'s last name is missing. Kindly provide a first name to proceed."
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
    }

    @BeforeEach
    fun beforeEach() {
        mockStrings()

        createEditPlayerViewModel = CreateEditPlayerViewModel(
            application = application,
            createFirebaseUserInfo = createFirebaseUserInfo,
            updateFirebaseUserInfo = updateFirebaseUserInfo,
            readFirebaseUserInfo = readFirebaseUserInfo,
            playerRepository = playerRepository,
            activeUserRepository = activeUserRepository,
            scope = scope,
            navigation = navigation,
            playersAdditionUpdates = playersAdditionUpdates,
            network = network
        )
    }

    @Nested
    inner class CheckForExistingPlayer {
        private val player = TestPlayer().create()

        @Test
        fun `when firstNameArgument is null should update toolbarNameResId to create player`() {
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState()
            )
            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

            createEditPlayerViewModel.checkForExistingPlayer(
                firstNameArgument = null,
                lastNameArgument = player.lastName
            )

            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(toolbarNameResId = StringsIds.createPlayer)
            )
        }

        @Test
        fun `when firstNameArgument is a empty string should update toolbarNameResId to create player`() {
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState()
            )
            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

            createEditPlayerViewModel.checkForExistingPlayer(
                firstNameArgument = "",
                lastNameArgument = player.lastName
            )

            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(toolbarNameResId = StringsIds.createPlayer)
            )
        }

        @Test
        fun `when lastNameArgument is null should update toolbarNameResId to create player`() {
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState()
            )
            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

            createEditPlayerViewModel.checkForExistingPlayer(
                firstNameArgument = player.firstName,
                lastNameArgument = null
            )

            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(toolbarNameResId = StringsIds.createPlayer)
            )
        }

        @Test
        fun `when lastNameArgument is a empty string should update toolbarNameResId to create player`() {
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState()
            )
            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

            createEditPlayerViewModel.checkForExistingPlayer(
                firstNameArgument = player.firstName,
                lastNameArgument = ""
            )

            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(toolbarNameResId = StringsIds.createPlayer)
            )
        }

        @Test
        fun `when firstNameArgument and lastNameArgument meets conditions and fetch player by name returns null should update toolbarNameResId to create player`() {
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState()
            )
            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns null

            createEditPlayerViewModel.checkForExistingPlayer(
                firstNameArgument = player.firstName,
                lastNameArgument = player.lastName
            )

            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(toolbarNameResId = StringsIds.createPlayer)
            )
        }

        @Test
        fun `when firstNameArgument and lastNameArgument meets conditions and fetch player by name returns player should update state for edit player`() {
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState()
            )
            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns player

            createEditPlayerViewModel.checkForExistingPlayer(
                firstNameArgument = player.firstName,
                lastNameArgument = player.lastName
            )

            Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, player)
            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(
                    firstName = player.firstName,
                    lastName = player.lastName,
                    editedPlayerUrl = player.imageUrl!!,
                    toolbarNameResId = StringsIds.editPlayer,
                    playerPositionString = "Center"
                )
            )
        }
    }

    @Test
    fun `update state for existing player`() {
        val player = TestPlayer().create()

        Assertions.assertEquals(
            createEditPlayerViewModel.createEditPlayerStateFlow.value,
            CreateEditPlayerState()
        )
        Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, null)

        createEditPlayerViewModel.updateStateForExistingPlayer(player = player)

        Assertions.assertEquals(createEditPlayerViewModel.editedPlayer, player)
        Assertions.assertEquals(
            createEditPlayerViewModel.createEditPlayerStateFlow.value,
            CreateEditPlayerState(
                firstName = player.firstName,
                lastName = player.lastName,
                editedPlayerUrl = player.imageUrl!!,
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Center"
            )
        )
    }

    @Test
    fun `update toolbar name res id state to create player`() {
        createEditPlayerViewModel.updateToolbarNameResIdStateToCreatePlayer()

        Assertions.assertEquals(
            createEditPlayerViewModel.createEditPlayerStateFlow.value,
            CreateEditPlayerState(toolbarNameResId = StringsIds.createPlayer)
        )
    }

    @Test
    fun `on toolbar menu clicked`() {
        createEditPlayerViewModel.onToolbarMenuClicked()

        verify { navigation.pop() }
    }

    @Nested
    inner class OnImageUploadClicked {

        @Test
        fun `when uri is set to null should call updateSheetToChooseFromGalleryOrTakePictureSheet`() {
            createEditPlayerViewModel.onImageUploadClicked(null)

            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(
                    sheet = Sheet(
                        title = "Choose Option",
                        values = listOf("Choose Image From Gallery", "Take A Picture")
                    )
                )
            )
        }

        @Test
        fun `when uri is not set to null should call updateSheetToRemoveImageSheet`() {
            val uriString = "uriString"

            every { uri.toString() } returns uriString

            createEditPlayerViewModel.onImageUploadClicked(uri = uri)

            Assertions.assertEquals(
                createEditPlayerViewModel.createEditPlayerStateFlow.value,
                CreateEditPlayerState(
                    sheet = Sheet(
                        title = "Choose Option",
                        values = listOf("Remove Image")
                    )
                )
            )
        }
    }

    @Nested
    inner class OnSelectedCreateEditImageOption {

        @Test
        fun `when option is choose from gallery should return choose image from gallery option`() {
            val option = createEditPlayerViewModel.onSelectedCreateEditImageOption(option = "Choose Image From Gallery")

            Assertions.assertEquals(option, CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY)
        }

        @Test
        fun `when option is take a picture should return take a picture option`() {
            val option = createEditPlayerViewModel.onSelectedCreateEditImageOption(option = "Take A Picture")

            Assertions.assertEquals(option, CreateEditImageOption.TAKE_A_PICTURE)
        }

        @Test
        fun `when option is remove image should return remove image option`() {
            val option = createEditPlayerViewModel.onSelectedCreateEditImageOption(option = "Remove Image")

            Assertions.assertEquals(option, CreateEditImageOption.REMOVE_IMAGE)
        }

        @Test
        fun `when option falls under else condition should return cancel option`() {
            val option = createEditPlayerViewModel.onSelectedCreateEditImageOption(option = "Cancel")

            Assertions.assertEquals(option, CreateEditImageOption.CANCEL)
        }
    }

    @Test
    fun `on navigate to app settings`() {
        createEditPlayerViewModel.onNavigateToAppSettings()

        verify { navigation.appSettings() }
    }

    @Test
    fun `permission not granted for camera alert`() {
        createEditPlayerViewModel.permissionNotGrantedForCameraAlert()

        verify { navigation.alert(alert = any()) }
    }

    @Test
    fun `permission not granted for read media or external storage alert`() {
        createEditPlayerViewModel.permissionNotGrantedForReadMediaOrExternalStorageAlert()

        verify { navigation.alert(alert = any()) }
    }

    @Nested
    inner class OnCreatePlayerClicked {

        @Test
        fun `when device connected to internet returns false should show alert`() = runTest {
            val uriString = "uriString"

            every { uri.toString() } returns uriString
            coEvery { network.isDeviceConnectedToInternet() } returns false

            createEditPlayerViewModel.onCreatePlayerClicked(uri = uri)

            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when device is connected to internet should enable progress and call validate player`() = runTest {
            val uriString = "uriString"

            every { uri.toString() } returns uriString
            coEvery { network.isDeviceConnectedToInternet() } returns true

            createEditPlayerViewModel.createEditPlayerMutableStateFlow.value = defaultState

            createEditPlayerViewModel.onCreatePlayerClicked(uri = uri)

            verify { navigation.enableProgress(progress = any()) }
            verify { createEditPlayerViewModel.validatePlayer(state = defaultState, uri = uri) }
        }
    }

    @Nested
    inner class ValidatePlayer {

        @Test
        fun `if first name is empty should show alert`() {
            createEditPlayerViewModel.validatePlayer(state = defaultState.copy(firstName = ""), uri = null)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `if last name is set to empty should show alert`() {
            createEditPlayerViewModel.validatePlayer(state = defaultState.copy(lastName = ""), uri = null)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `if first or last name is not empty should call determine creating or editing player`() {
            createEditPlayerViewModel.validatePlayer(state = defaultState, uri = null)

            verify { createEditPlayerViewModel.determineCreatingOrEditingPlayer(state = defaultState, uri = null) }
        }
    }

    @Nested
    inner class DetermineCreatingOrEditingPlayer {
        private val player = TestPlayer().create()

        @Test
        fun `when editedPlayer is set to null should call check if player already exists`() = runTest {
            createEditPlayerViewModel.editedPlayer = null

            createEditPlayerViewModel.determineCreatingOrEditingPlayer(state = defaultState, uri = null)

            verify { createEditPlayerViewModel.checkIfPlayerAlreadyExists(state = defaultState, uri = null) }
        }

        @Test
        fun `when editedPlayer is not set to null and hasNotEditedExistingPlayer is set to true should show alert`() {
            val currentPlayerHasNoChangesDescription = ">There haven\\'t been any recent updates or modifications to the current player. Please make adjustments to the existing player to proceed."
            val noChangesMade = "No Changes Made"
            val gotIt = "Got It"

            every { application.getString(StringsIds.currentPlayerHasNoChangesDescription) } returns currentPlayerHasNoChangesDescription
            every { application.getString(StringsIds.noChangesMade) } returns noChangesMade
            every { application.getString(StringsIds.gotIt) } returns gotIt
            every { application.getString(StringsIds.center) } returns "Center"

            val state = CreateEditPlayerState(
                firstName = player.firstName,
                lastName = player.lastName,
                editedPlayerUrl = player.imageUrl!!,
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Center"
            )
            val expectedAlert = Alert(
                title = noChangesMade,
                dismissButton = AlertConfirmAndDismissButton(buttonText = gotIt),
                description = currentPlayerHasNoChangesDescription
            )
            createEditPlayerViewModel.editedPlayer = player

            createEditPlayerViewModel.determineCreatingOrEditingPlayer(state = state, uri = null)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = expectedAlert) }
        }

        @Test
        fun `when editedPlayer is not set to null and editedPlayerUrl is empty should call checkImageUri`() = runTest {
            val newFirstName = "newFirstName"

            val state = CreateEditPlayerState(
                firstName = newFirstName,
                lastName = player.lastName,
                editedPlayerUrl = "",
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Center"
            )

            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns null
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName)!!.firebaseKey } returns ""
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { activeUserRepository.fetchActiveUser()!!.firebaseAccountInfoKey } returns null

            createEditPlayerViewModel.editedPlayer = player

            createEditPlayerViewModel.determineCreatingOrEditingPlayer(state = state, uri = null)

            verify { createEditPlayerViewModel.checkImageUri(uri = null, state = state) }
        }

        @Test
        fun `when editedPlayer is not set to null and editedPlayerUrl is not empty should call updateUserInFirebase`() = runTest {
            val newFirstName = "newFirstName"

            val state = CreateEditPlayerState(
                firstName = newFirstName,
                lastName = player.lastName,
                editedPlayerUrl = player.imageUrl!!,
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Center"
            )

            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName) } returns null
            coEvery { playerRepository.fetchPlayerByName(firstName = player.firstName, lastName = player.lastName)!!.firebaseKey } returns ""
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { activeUserRepository.fetchActiveUser()!!.firebaseAccountInfoKey } returns null

            createEditPlayerViewModel.editedPlayer = player

            createEditPlayerViewModel.determineCreatingOrEditingPlayer(state = state, uri = null)

            coVerify { createEditPlayerViewModel.updateUserInFirebase(state = state, imageUrl = player.imageUrl!!) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Test
    fun `on clear image state`() {
        createEditPlayerViewModel.onClearImageState()

        Assertions.assertEquals(
            createEditPlayerViewModel.createEditPlayerStateFlow.value,
            CreateEditPlayerState(editedPlayerUrl = "")
        )
    }

    @Nested
    inner class HasNotEditedExistingPlayer {
        private val player = TestPlayer().create()

        @Test
        fun `when first name of player is diffrent then the update player first name should return false`() {
            val newFirstName = "newFirstName"
            val existingPlayer = player
            val state = CreateEditPlayerState(
                firstName = newFirstName,
                lastName = player.lastName,
                editedPlayerUrl = player.imageUrl!!,
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Center"
            )

            Assertions.assertEquals(
                createEditPlayerViewModel.hasNotEditedExistingPlayer(existingPlayer = existingPlayer, uri = null, state = state),
                false
            )
        }

        @Test
        fun `when last name of player is diffrent then the update player last name should return false`() {
            val newLastName = "newLastName"
            val existingPlayer = player
            val state = CreateEditPlayerState(
                firstName = player.firstName,
                lastName = newLastName,
                editedPlayerUrl = player.imageUrl!!,
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Center"
            )

            Assertions.assertEquals(
                createEditPlayerViewModel.hasNotEditedExistingPlayer(existingPlayer = existingPlayer, uri = null, state = state),
                false
            )
        }

        @Test
        fun `when position of player is diffrent then the update player position should return false`() {
            every { application.getString(StringsIds.pointGuard) } returns "Point Guard"
            val existingPlayer = player
            val state = CreateEditPlayerState(
                firstName = player.firstName,
                lastName = player.lastName,
                editedPlayerUrl = player.imageUrl!!,
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = "Point Guard"
            )

            Assertions.assertEquals(
                createEditPlayerViewModel.hasNotEditedExistingPlayer(existingPlayer = existingPlayer, uri = null, state = state),
                false
            )
        }

//        @Test
//        fun `when position of player is diffrent then the update player position should return false`() {
//            every { application.getString(StringsIds.pointGuard) } returns "Point Guard"
//            val existingPlayer = player
//            val state = CreateEditPlayerState(
//                firstName = player.firstName,
//                lastName = player.lastName,
//                editedPlayerUrl = player.imageUrl!!,
//                toolbarNameResId = StringsIds.editPlayer,
//                playerPositionString = "Point Guard"
//            )
//
//            Assertions.assertEquals(
//                createEditPlayerViewModel.hasNotEditedExistingPlayer(existingPlayer = existingPlayer, uri = null, state = state),
//                false
//            )
//        }
    }

    @Nested
    inner class CheckIfPlayerAlreadyExists {

        @Test
        fun `if fetch player by name returns null should call checkImageUri`() = runTest {
            coEvery { playerRepository.fetchPlayerByName(firstName = defaultState.firstName, lastName = defaultState.lastName) } returns null
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { activeUserRepository.fetchActiveUser()!!.firebaseAccountInfoKey } returns null

            createEditPlayerViewModel.checkIfPlayerAlreadyExists(state = defaultState, uri = null)

            verify { createEditPlayerViewModel.checkImageUri(state = defaultState, uri = null) }
            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `if fetch player does not return null should call alert`() = runTest {
            val uriString = "uriString"
            val player = TestPlayer().create()

            every { uri.toString() } returns uriString
            coEvery { playerRepository.fetchPlayerByName(firstName = defaultState.firstName, lastName = defaultState.lastName) } returns player

            createEditPlayerViewModel.checkIfPlayerAlreadyExists(state = defaultState, uri = uri)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class CheckImageUri {

        @Test
        fun `when uri passed in is set to null should call createUserInFirebase`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { activeUserRepository.fetchActiveUser()!!.firebaseAccountInfoKey } returns null

            createEditPlayerViewModel.checkImageUri(state = defaultState, uri = null)

            coVerify { createEditPlayerViewModel.createUserInFirebase(state = defaultState, imageUrl = null) }
        }

        @Test
        fun `when uri passed in is not null and create image firebase storage returns a null imageUrl should call alert`() = runTest {
            val uriString = "uriString"

            every { uri.toString() } returns uriString
            coEvery { createFirebaseUserInfo.attemptToCreateImageFirebaseStorageResponseFlow(uri = uri) } returns flowOf(value = null)

            createEditPlayerViewModel.checkImageUri(state = defaultState, uri = uri)

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when uri passed in is not null and create image firebase storage returns a valid imageUrl should call createUserInFirebase`() = runTest {
            val uriString = "uriString"

            every { uri.toString() } returns uriString
            coEvery { createFirebaseUserInfo.attemptToCreateImageFirebaseStorageResponseFlow(uri = uri) } returns flowOf(value = uriString)
            coEvery { activeUserRepository.fetchActiveUser() } returns null
            coEvery { activeUserRepository.fetchActiveUser()!!.firebaseAccountInfoKey } returns null

            createEditPlayerViewModel.checkImageUri(state = defaultState, uri = uri)

            coVerify { createEditPlayerViewModel.createUserInFirebase(state = defaultState, imageUrl = uriString) }
        }
    }

    @Nested
    inner class CreateUserInFirebase {
        private val activeUser = TestActiveUser().create()

        @Test
        fun `when key is empty should call alert`() = runTest {
            coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(firebaseAccountInfoKey = "")

            createEditPlayerViewModel.createUserInFirebase(state = defaultState, imageUrl = "")

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when key is not empty and attempt to create firebase response flow returns a boolean should call alert`() = runTest {
            val key = "key1"

            coEvery { activeUserRepository.fetchActiveUser() } returns activeUser.copy(firebaseAccountInfoKey = key)
            coEvery {
                createFirebaseUserInfo.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
                    key = key,
                    playerInfoRealtimeResponse = PlayerInfoRealtimeResponse(
                        firstName = defaultState.firstName,
                        lastName = defaultState.lastName,
                        positionValue = defaultState.playerPositionString.toPlayerPosition(application = application).value,
                        imageUrl = ""
                    )
                )
            } returns flowOf(value = false)

            createEditPlayerViewModel.createUserInFirebase(state = defaultState, imageUrl = "")

            coVerify {
                createEditPlayerViewModel.handleFirebaseResponseForSavingPlayer(
                    isSuccessful = false,
                    key = key,
                    state = defaultState,
                    imageUrl = ""
                )
            }
        }
    }

    @Nested
    inner class HandleFirebaseResponseForSavingPlayer {
        private val key = "key1"

        @Test
        fun `when isSuccessful is set to true should call updatePlayerInstance`() = runTest {
            val playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse> = emptyList()

            coEvery { readFirebaseUserInfo.getPlayerInfoList(key) } returns flowOf(value = playerInfoRealtimeWithKeyResponseList)

            createEditPlayerViewModel.handleFirebaseResponseForSavingPlayer(
                isSuccessful = true,
                key = key,
                state = defaultState,
                imageUrl = ""
            )

            coVerify {
                createEditPlayerViewModel.updatePlayerInstance(
                    playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                    state = defaultState,
                    imageUrl = ""
                )
            }
        }

        @Test
        fun `when isSuccessful is set to false should call alert`() = runTest {
            createEditPlayerViewModel.handleFirebaseResponseForSavingPlayer(
                isSuccessful = false,
                key = key,
                state = defaultState,
                imageUrl = ""
            )

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }
    }

    @Nested
    inner class UpdatePlayerInstance {

        @Test
        fun `when passed in list is empty should call alert`() = runTest {
            val playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse> = emptyList()

            createEditPlayerViewModel.updatePlayerInstance(
                playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                state = defaultState,
                imageUrl = null
            )

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when passed in list is not empty should call handle saving player`() = runTest {
            val playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse> = listOf(TestPlayerInfoRealtimeWithKeyResponse().create())

            createEditPlayerViewModel.updatePlayerInstance(
                playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                state = defaultState,
                imageUrl = null
            )

            coVerify {
                createEditPlayerViewModel.handleSavingPlayer(
                    playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                    state = defaultState,
                    imageUrl = null
                )
            }
        }
    }

    @Nested
    inner class HandleSavingPlayer {

        @Test
        fun `when recentlySavedPlayer is null should show alert`() = runTest {
            val playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse> = listOf(
                TestPlayerInfoRealtimeWithKeyResponse().create().copy(
                    playerInfo = TestPlayerInfoRealtimeResponse().create().copy(firstName = "firstName1", lastName = "lastName1")
                )
            )

            createEditPlayerViewModel.handleSavingPlayer(
                playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                state = defaultState,
                imageUrl = null
            )

            verify { navigation.disableProgress() }
            verify { navigation.alert(alert = any()) }
        }

        @Test
        fun `when recentlySavedPlayer is not null should pop and create player instance`() = runTest {
            val playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse> = listOf(
                TestPlayerInfoRealtimeWithKeyResponse().create().copy(
                    playerInfo = TestPlayerInfoRealtimeResponse().create().copy(firstName = defaultState.firstName, lastName = defaultState.lastName)
                )
            )
            val key = PLAYER_FIREBASE_KEY
            val state = defaultState

            createEditPlayerViewModel.handleSavingPlayer(
                playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                state = defaultState,
                imageUrl = null
            )

            val player = Player(
                firstName = state.firstName,
                lastName = state.lastName,
                position = state.playerPositionString.toPlayerPosition(application = application),
                firebaseKey = key,
                imageUrl = ""
            )

            coVerify { playerRepository.createPlayer(player = player) }
            coVerify { playersAdditionUpdates.updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded = true) }

            verify { navigation.disableProgress() }
            verify { navigation.pop() }
        }
    }

    @Test
    fun `onFirstNameValueChanged should update firstName State property`() {
        val newFirstName = "newFirstName"

        createEditPlayerViewModel.onFirstNameValueChanged(newFirstName = newFirstName)

        Assertions.assertEquals(createEditPlayerViewModel.createEditPlayerMutableStateFlow.value.firstName, newFirstName)
    }

    @Test
    fun `onLastNameValueChanged should update lastName state property`() {
        val newLastName = "newLastName"

        createEditPlayerViewModel.onLastNameValueChanged(newLastName = newLastName)

        Assertions.assertEquals(createEditPlayerViewModel.createEditPlayerMutableStateFlow.value.lastName, newLastName)
    }

    @Test
    fun `onPlayerPositionStringChanged should update playerPositionStringResId state property`() {
        val newPositionString = "pg"

        createEditPlayerViewModel.onPlayerPositionStringChanged(newPositionString = newPositionString)

        Assertions.assertEquals(createEditPlayerViewModel.createEditPlayerMutableStateFlow.value.playerPositionString, newPositionString)
    }

    @Test
    fun `camera permission not granted alert`() {
        val alert = createEditPlayerViewModel.cameraPermissionNotGrantedAlert()

        Assertions.assertEquals(alert.title, "Permission has been declined")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Settings")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Not Now")
        Assertions.assertEquals(alert.description, "Camera permission has been denied. To manually grant permission for the camera and upload pictures for the Player, kindly navigate to Settings.")
    }

    @Test
    fun `media or external storage not granted alert when should ask for permission is not enabled`() {
        val alert = createEditPlayerViewModel.mediaOrExternalStorageNotGrantedAlert(shouldAskForPermission = false)

        Assertions.assertEquals(alert.title, "Permission has been declined")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Settings")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Not Now")
        Assertions.assertEquals(alert.description, "Permission to read external storage has been declined. To manually enable the \"Read External Storage\" permission and select an image from the gallery for the Player, please navigate to settings.")
    }

    @Test
    fun `media or external storage not granted alert when should ask for permission is enabled`() {
        val alert = createEditPlayerViewModel.mediaOrExternalStorageNotGrantedAlert(shouldAskForPermission = true)

        Assertions.assertEquals(alert.title, "Permission has been declined")
        Assertions.assertEquals(alert.confirmButton!!.buttonText, "Settings")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Not Now")
        Assertions.assertEquals(alert.description, "Permission to read external storage has been declined. To manually enable the \"Read External Storage\" permission and select an image from the gallery for the Player, please navigate to settings.")
    }

    @Test
    fun `first name empty alert`() {
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.firstNameEmptyAlert()

        Assertions.assertEquals(alert.title, "No First Name Entered")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "The player\\'s first name is missing. Kindly provide a first name to proceed.")
    }

    @Test
    fun `last name empty alert`() {
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.lastNameEmptyAlert()

        Assertions.assertEquals(alert.title, "No Last Name Entered")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "The player\\'s last name is missing. Kindly provide a first name to proceed.")
    }

    @Test
    fun `not connected to internet alert`() {
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.notConnectedToInternetAlert()

        Assertions.assertEquals(alert.title, "Not connected to internet")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "We have detected currently not connected to internet. Please connect to service, and try again.")
    }

    @Test
    fun `not able to upload image alert`() {
        val alert = createEditPlayerViewModel.notAbleToUploadImageAlert()

        Assertions.assertEquals(alert.title, "Unable to upload image")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Ok")
        Assertions.assertEquals(alert.description, "The image upload was unsuccessful. Please attempt it once more.")
    }

    @Test
    fun `we have detected a problem with your account alert`() {
        every { application.getString(StringsIds.issueOccurred) } returns "Issue Occurred"
        every { application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue) } returns "We have detected a problem with your account. Please contact support to resolve issue."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.weHaveDetectedAProblemWithYourAccountAlert()

        Assertions.assertEquals(alert.title, "Issue Occurred")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "We have detected a problem with your account. Please contact support to resolve issue.")
    }

    @Test
    fun `we were not able to create the player alert`() {
        every { application.getString(StringsIds.issueOccurred) } returns "Issue Occurred"
        every { application.getString(StringsIds.playerCreationFailedPleaseTryAgain) } returns "Player creation failed. Please try again."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.weWereNotAbleToCreateThePlayerAlert()

        Assertions.assertEquals(alert.title, "Issue Occurred")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "Player creation failed. Please try again.")
    }

    @Test
    fun `your player could not be retrieved alert`() {
        every { application.getString(StringsIds.issueOccurred) } returns "Issue Occurred"
        every { application.getString(StringsIds.yourPlayerCouldNotBeRetrievedDescription) } returns "Your player could not be retrieved. Please try again."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.yourPlayerCouldNotBeRetrievedAlert()

        Assertions.assertEquals(alert.title, "Issue Occurred")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "Your player could not be retrieved. Please try again.")
    }

    @Test
    fun `player already has been added alert`() {
        every { application.getString(StringsIds.issueOccurred) } returns "Issue Occurred"
        every { application.getString(StringsIds.playerAlreadyHasBeenAddedDescription) } returns "The specified player name has already been included. Kindly add a new player with a name that has not been added before."
        every { application.getString(StringsIds.gotIt) } returns "Got It"

        val alert = createEditPlayerViewModel.playerAlreadyHasBeenAddedAlert()

        Assertions.assertEquals(alert.title, "Issue Occurred")
        Assertions.assertEquals(alert.dismissButton!!.buttonText, "Got It")
        Assertions.assertEquals(alert.description, "The specified player name has already been included. Kindly add a new player with a name that has not been added before.")
    }

    @Test
    fun `remove image sheet`() {
        every { application.getString(StringsIds.chooseOption) } returns "Choose Option"
        every { application.getString(StringsIds.removeImage) } returns "Remove Image"

        val sheet = createEditPlayerViewModel.removeImageSheet()

        Assertions.assertEquals(sheet.title, "Choose Option")
        Assertions.assertEquals(sheet.values, listOf("Remove Image"))
    }

    @Test
    fun `choose from gallery or take picture sheet`() {
        every { application.getString(StringsIds.chooseOption) } returns "Choose Option"
        every { application.getString(StringsIds.chooseImageFromGallery) } returns "Choose Image From Gallery"
        every { application.getString(StringsIds.takeAPicture) } returns "Take A Picture"

        val sheet = createEditPlayerViewModel.chooseFromGalleryOrTakePictureSheet()

        Assertions.assertEquals(sheet.title, "Choose Option")
        Assertions.assertEquals(sheet.values, listOf("Choose Image From Gallery", "Take A Picture"))
    }
}

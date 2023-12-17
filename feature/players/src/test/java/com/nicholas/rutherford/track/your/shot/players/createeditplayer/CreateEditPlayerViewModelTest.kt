package com.nicholas.rutherford.track.your.shot.players.createeditplayer

import android.app.Application
import android.net.Uri
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
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
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.toPlayerPosition
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
        playerPositionStringResId = StringsIds.pg,
        sheet = null
    )

    private fun mockStrings() {
        every { application.getString(StringsIds.chooseOption) } returns "Choose Option"
        every { application.getString(StringsIds.chooseImageFromGallery) } returns "Choose Image From Gallery"
        every { application.getString(StringsIds.takeAPicture) } returns "Take A Picture"
        every { application.getString(StringsIds.removeImage) } returns "Remove Image"
        every { application.getString(StringsIds.permissionHasBeenDeclined) } returns "Permission Has Been Declined"
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
            readFirebaseUserInfo = readFirebaseUserInfo,
            playerRepository = playerRepository,
            activeUserRepository = activeUserRepository,
            scope = scope,
            navigation = navigation,
            playersAdditionUpdates = playersAdditionUpdates,
            network = network
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
        fun `when option falls under else conidtion should return cancel option`() {
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
        fun `if first or last name is not empty should call check if player already exists`() {
            createEditPlayerViewModel.validatePlayer(state = defaultState, uri = null)

            verify { createEditPlayerViewModel.checkIfPlayerAlreadyExists(state = defaultState, uri = null) }
        }
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
                        positionValue = defaultState.playerPositionStringResId.toPlayerPosition().value,
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

            coVerify { createEditPlayerViewModel.handleSavingPlayer(
                playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                state = defaultState,
                imageUrl = null
            ) }
        }
    }
}

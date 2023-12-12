package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.shouldAskForReadMediaImages
import com.nicholas.rutherford.track.your.shot.helper.extensions.toPlayerPosition
import com.nicholas.rutherford.track.your.shot.helper.extensions.toPlayerPositionAbvId
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreatePlayerViewModel(
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val scope: CoroutineScope,
    private val navigation: CreatePlayerNavigation,
    private val playersAdditionUpdates: PlayersAdditionUpdates,
    private val network: Network
) : ViewModel() {

    internal val createPlayerMutableStateFlow = MutableStateFlow(value = CreatePlayerState())
    val createPlayerStateFlow = createPlayerMutableStateFlow.asStateFlow()

    fun onToolbarMenuClicked() = navigation.pop()

    fun onImageUploadClicked(uri: Uri?) {
        if (uri == null) {
            createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(
                sheet = Sheet(
                    title = application.getString(StringsIds.chooseOption),
                    values = listOf(
                        application.getString(StringsIds.chooseImageFromGallery),
                        application.getString(StringsIds.takeAPicture)
                    )
                )
            )
        } else {
            createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(
                sheet = Sheet(
                    title = application.getString(StringsIds.chooseOption),
                    values = listOf(application.getString(StringsIds.removeImage))
                )
            )
        }
    }

    fun onSelectedCreateEditImageOption(option: String): CreateEditImageOption {
        return when (option) {
            application.getString(StringsIds.chooseImageFromGallery) -> { CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY }
            application.getString(StringsIds.takeAPicture) -> { CreateEditImageOption.TAKE_A_PICTURE }
            application.getString(StringsIds.removeImage) -> { CreateEditImageOption.REMOVE_IMAGE }
            else -> { CreateEditImageOption.CANCEL }
        }
    }

    internal fun onNavigateToAppSettings() = navigation.appSettings()

    fun permissionNotGrantedForCameraAlert() {
        navigation.alert(alert = cameraPermissionNotGrantedAlert())
    }

    fun permissionNotGrantedForReadMediaOrExternalStorageAlert() {
        navigation.alert(alert = mediaOrExternalStorageNotGrantedAlert())
    }

    fun onCreatePlayerClicked(uri: Uri?) {
        val state = createPlayerMutableStateFlow.value

        navigation.enableProgress(progress = Progress())

        validatePlayer(state = state, uri = uri)
    }

    fun validatePlayer(state: CreatePlayerState, uri: Uri?) {
        if (state.firstName.isEmpty()) {
            navigation.disableProgress()
            navigation.alert(alert = firstNameEmptyAlert())
        } else if (state.lastName.isEmpty()) {
            navigation.disableProgress()
            navigation.alert(alert = lastNameEmptyAlert())
        } else {
            checkImageUri(uri = uri, state = state)
        }
    }

    fun checkImageUri(state: CreatePlayerState, uri: Uri?) {
        scope.launch {
            uri?.let { playerUri ->
                println(playerUri.toString())
                createFirebaseUserInfo.attemptToCreateImageFirebaseStorageResponseFlow(uri = playerUri)
                    .collectLatest { imageUrl ->
                        if (imageUrl != null) {
                            savePlayer(state = state, imageUrl = imageUrl)
                        } else {
                            // show error unable to upload image
                        }
                    }
            } ?: run {
                savePlayer(state = state, imageUrl = null)
            }
        }
    }

    suspend fun savePlayer(state: CreatePlayerState, imageUrl: String?) {
        val key = activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey ?: ""

        if (key.isNotEmpty()) {
            createFirebaseUserInfo.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
                key = key,
                playerInfoRealtimeResponse = PlayerInfoRealtimeResponse(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    positionValue = state.playerPositionStringResId.toPlayerPositionAbvId() ?: PlayerPositions.None.value,
                    imageUrl = imageUrl ?: ""
                )
            )
                .collectLatest {
                    if (it == true) {
                        readFirebaseUserInfo.getPlayerInfoList(key)
                            .collectLatest { playerInfoRealtimeWithKeyResponse ->
                                if (playerInfoRealtimeWithKeyResponse.isNotEmpty()) {
                                    var recentlySavedPlayer: PlayerInfoRealtimeWithKeyResponse? = null
                                    playerInfoRealtimeWithKeyResponse.map { player ->
                                        if (player.playerInfo.firstName == state.firstName && player.playerInfo.lastName == state.lastName) {
                                            recentlySavedPlayer = player
                                        }
                                    }
                                    recentlySavedPlayer?.let { response ->
                                        val playerKey = response.playerFirebaseKey
                                        val player = Player(
                                            firstName = state.firstName,
                                            lastName = state.lastName,
                                            position = state.playerPositionStringResId.toPlayerPosition(),
                                            firebaseKey = playerKey,
                                            imageUrl = imageUrl ?: ""
                                        )
                                        playerRepository.createPlayer(player = player)
                                        playersAdditionUpdates.updateNewPlayerAddedFlow(player = player)
                                        navigation.disableProgress()
                                        navigation.navigateToPlayersList()
                                    } ?: run  {
                                        // to do add this
                                    }

                                } else {
                                    // show a error
                                }
                            }
                    } else {

                    }
                }
        } else {
            // underlining problem here
        }
    }

    fun onFirstNameValueChanged(newFirstName: String) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(firstName = newFirstName)
    }

    fun onLastNameValueChanged(newLastName: String) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(lastName = newLastName)
    }

    fun cameraPermissionNotGrantedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.permissionHasBeenDeclined),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.settings),
                onButtonClicked = { onNavigateToAppSettings() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.notNow)
            ),
            description = application.getString(StringsIds.cameraPermissionHasBeenDeniedDescription)
        )
    }

    fun mediaOrExternalStorageNotGrantedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.permissionHasBeenDeclined),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.settings),
                onButtonClicked = { onNavigateToAppSettings() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.notNow)
            ),
            description = if (!shouldAskForReadMediaImages()) {
                application.getString(StringsIds.readExternalStorageDescription)
            } else {
                application.getString(StringsIds.readMediaImagesDescription)
            }
        )
    }

    fun firstNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noFirstNameEntered),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playersFirstNameEmptyDescription)
        )
    }

    fun lastNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noLastNameEntered),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playersLastNameEmptyDescription)
        )
    }
}

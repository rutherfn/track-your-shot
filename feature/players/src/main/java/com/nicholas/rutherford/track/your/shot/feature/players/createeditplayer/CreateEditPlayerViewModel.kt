package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
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
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CreateEditPlayerViewModel(
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val scope: CoroutineScope,
    private val navigation: CreateEditPlayerNavigation,
    private val playersAdditionUpdates: PlayersAdditionUpdates,
    private val network: Network
) : ViewModel() {

    internal val createPlayerMutableStateFlow = MutableStateFlow(value = CreateEditPlayerState())
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
        navigation.alert(alert = mediaOrExternalStorageNotGrantedAlert(shouldAskForPermission = shouldAskForReadMediaImages()))
    }

    fun onCreatePlayerClicked(uri: Uri?) {
        scope.launch {
            if (network.isDeviceConnectedToInternet()) {
                val state = createPlayerMutableStateFlow.value

                navigation.enableProgress(progress = Progress())

                validatePlayer(state = state, uri = uri)
            } else {
                navigation.disableProgress()
                navigation.alert(alert = notConnectedToInternetAlert())
            }
        }
    }

    fun validatePlayer(state: CreateEditPlayerState, uri: Uri?) {
        if (state.firstName.isEmpty()) {
            navigation.disableProgress()
            navigation.alert(alert = firstNameEmptyAlert())
        } else if (state.lastName.isEmpty()) {
            navigation.disableProgress()
            navigation.alert(alert = lastNameEmptyAlert())
        } else {
            checkIfPlayerAlreadyExists(uri = uri, state = state)
        }
    }

    fun checkIfPlayerAlreadyExists(state: CreateEditPlayerState, uri: Uri?) {
        scope.launch {
            val player = playerRepository.fetchPlayerByName(
                firstName = state.firstName,
                lastName = state.lastName
            )

            if (player != null) {
                navigation.disableProgress()
                navigation.alert(alert = playerAlreadyHasBeenAddedAlert())
            } else {
                checkImageUri(uri = uri, state = state)
            }
        }
    }

    fun checkImageUri(state: CreateEditPlayerState, uri: Uri?) {
        scope.launch {
            uri?.let { playerUri ->
                createFirebaseUserInfo.attemptToCreateImageFirebaseStorageResponseFlow(uri = playerUri)
                    .collectLatest { imageUrl ->
                        if (imageUrl != null) {
                            createUserInFirebase(state = state, imageUrl = imageUrl)
                        } else {
                            navigation.disableProgress()
                            navigation.alert(alert = notAbleToUploadImageAlert())
                        }
                    }
            } ?: run {
                createUserInFirebase(state = state, imageUrl = null)
            }
        }
    }

    suspend fun createUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
        val key = activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey ?: ""

        if (key.isNotEmpty()) {
            createFirebaseUserInfo.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
                key = key,
                playerInfoRealtimeResponse = PlayerInfoRealtimeResponse(
                    firstName = state.firstName,
                    lastName = state.lastName,
                    positionValue = state.playerPositionStringResId.toPlayerPosition().value,
                    imageUrl = imageUrl ?: ""
                )
            ).collectLatest { isSuccessful ->
                    if (isSuccessful) {
                        updatePlayerInstance(
                            key = key,
                            state = state,
                            imageUrl = imageUrl
                        )
                    } else {
                        navigation.disableProgress()
                        navigation.alert(alert = weWereNotAbleToCreateThePlayerAlert())
                    }
                }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    suspend fun updatePlayerInstance(key: String, state: CreateEditPlayerState, imageUrl: String?) {
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
                        navigation.pop()
                    } ?: run  {
                        navigation.disableProgress()
                        navigation.alert(alert = yourPlayerCouldNotBeRetrievedAlert())
                    }

                } else {
                    navigation.disableProgress()
                    navigation.alert(alert = yourPlayerCouldNotBeRetrievedAlert())
                }
            }
    }

    fun onFirstNameValueChanged(newFirstName: String) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(firstName = newFirstName)
    }

    fun onLastNameValueChanged(newLastName: String) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(lastName = newLastName)
    }

    fun onPlayerPositionStringResIdValueChanged(newPositionStringResId: Int) {
        createPlayerMutableStateFlow.value = createPlayerMutableStateFlow.value.copy(playerPositionStringResId = newPositionStringResId)
    }

    internal fun cameraPermissionNotGrantedAlert(): Alert {
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

    internal fun mediaOrExternalStorageNotGrantedAlert(shouldAskForPermission: Boolean): Alert {
        return Alert(
            title = application.getString(StringsIds.permissionHasBeenDeclined),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.settings),
                onButtonClicked = { onNavigateToAppSettings() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.notNow)
            ),
            description = if (!shouldAskForPermission) {
                application.getString(StringsIds.readExternalStorageDescription)
            } else {
                application.getString(StringsIds.readMediaImagesDescription)
            }
        )
    }

    internal fun firstNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noFirstNameEntered),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playersFirstNameEmptyDescription)
        )
    }

    internal fun lastNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noLastNameEntered),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playersLastNameEmptyDescription)
        )
    }

    internal fun notConnectedToInternetAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.notConnectedToInternet),
            description = application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    internal fun notAbleToUploadImageAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToUploadImage),
            description = application.getString(StringsIds.theImageUploadWasUnsuccessful),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.ok))
        )
    }

    internal fun weHaveDetectedAProblemWithYourAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    internal fun weWereNotAbleToCreateThePlayerAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerCreationFailedPleaseTryAgain),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    internal fun yourPlayerCouldNotBeRetrievedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.yourPlayerCouldNotBeRetrievedDescription),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }

    internal fun playerAlreadyHasBeenAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerAlreadyHasBeenAddedDescription),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt))
        )
    }
}

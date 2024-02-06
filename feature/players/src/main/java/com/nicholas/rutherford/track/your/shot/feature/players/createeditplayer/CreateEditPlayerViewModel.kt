package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.Center.toPlayerPosition
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.shouldAskForReadMediaImages
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditPlayerViewModel(
    private val application: Application,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val scope: CoroutineScope,
    private val navigation: CreateEditPlayerNavigation,
    private val playersAdditionUpdates: PlayersAdditionUpdates,
    private val network: Network
) : ViewModel() {

    internal val createEditPlayerMutableStateFlow = MutableStateFlow(value = CreateEditPlayerState())
    val createEditPlayerStateFlow = createEditPlayerMutableStateFlow.asStateFlow()

    internal var editedPlayer: Player? = null

    fun checkForExistingPlayer(firstNameArgument: String?, lastNameArgument: String?) {
        scope.launch {
            safeLet(firstNameArgument, lastNameArgument) { firstName, lastName ->
                if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    playerRepository.fetchPlayerByName(firstName = firstName, lastName = lastName)
                        ?.let { player ->
                            updateStateForExistingPlayer(player = player)
                        } ?: run { updateToolbarNameResIdStateToCreatePlayer() }
                } else {
                    updateToolbarNameResIdStateToCreatePlayer()
                }
            } ?: run {
                updateToolbarNameResIdStateToCreatePlayer()
            }
        }
    }

    internal fun hintLogNewShotText(firstName: String? = null, lastName: String? = null): String {
        var hintLogNewShotText: String = createEditPlayerMutableStateFlow.value.hintLogNewShotText

        safeLet(firstName, lastName) { first, last ->
            hintLogNewShotText = if (first.isNotEmpty() && last.isNotEmpty()) {
                application.getString(R.string.hint_log_new_shots_for_player_x, "$first $last")
            } else if (first.isNotEmpty()) {
                application.getString(R.string.hint_log_new_shots_for_player_x, first)
            } else if (last.isNotEmpty()) {
                application.getString(R.string.hint_log_new_shots_for_player_x, last)
            } else {
                application.getString(StringsIds.hintLogNewShots)
            }
        } ?: run {
            hintLogNewShotText = application.getString(StringsIds.hintLogNewShots)
        }

        return hintLogNewShotText
    }

    internal fun updateStateForExistingPlayer(player: Player) {
        editedPlayer = player
        createEditPlayerMutableStateFlow.value =
            createEditPlayerMutableStateFlow.value.copy(
                firstName = player.firstName,
                lastName = player.lastName,
                editedPlayerUrl = player.imageUrl ?: "",
                toolbarNameResId = StringsIds.editPlayer,
                playerPositionString = application.getString(player.position.toType()),
                hintLogNewShotText = hintLogNewShotText(firstName = player.firstName, lastName = player.lastName),
                shotsHaveBeenLogged = player.shotsLoggedList.isNotEmpty()
            )
    }

    fun updateToolbarNameResIdStateToCreatePlayer() {
        createEditPlayerMutableStateFlow.update { state ->
            state.copy(
                toolbarNameResId = StringsIds.createPlayer,
                hintLogNewShotText = hintLogNewShotText(firstName = null, lastName = null)
            )
        }
    }

    fun onToolbarMenuClicked() = navigation.pop()

    fun onImageUploadClicked(uri: Uri?) {
        when {
            editedPlayer != null && createEditPlayerStateFlow.value.editedPlayerUrl.isNotEmpty() ->
                updateSheetToRemoveImageSheet()
            uri == null -> updateSheetToChooseFromGalleryOrTakePictureSheet()
            else -> updateSheetToRemoveImageSheet()
        }
    }

    private fun updateSheetToChooseFromGalleryOrTakePictureSheet() {
        createEditPlayerMutableStateFlow.value = createEditPlayerMutableStateFlow.value.copy(
            sheet = chooseFromGalleryOrTakePictureSheet()
        )
    }

    private fun updateSheetToRemoveImageSheet() {
        createEditPlayerMutableStateFlow.value = createEditPlayerMutableStateFlow.value.copy(
            sheet = removeImageSheet()
        )
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
                val state = createEditPlayerMutableStateFlow.value

                navigation.enableProgress(progress = Progress())

                validatePlayer(state = state, uri = uri)
            } else {
                navigation.alert(alert = notConnectedToInternetAlert())
            }
        }
    }

    internal fun validatePlayer(state: CreateEditPlayerState, uri: Uri?) {
        if (state.firstName.isEmpty()) {
            navigation.disableProgress()
            navigation.alert(alert = firstNameEmptyAlert())
        } else if (state.lastName.isEmpty()) {
            navigation.disableProgress()
            navigation.alert(alert = lastNameEmptyAlert())
        } else {
            determineCreatingOrEditingPlayer(state = state, uri = uri)
        }
    }

    internal fun determineCreatingOrEditingPlayer(state: CreateEditPlayerState, uri: Uri?) {
        editedPlayer?.let { player ->
            if (hasNotEditedExistingPlayer(existingPlayer = player, uri = uri, state = state)) {
                navigation.disableProgress()
                navigation.alert(alert = noChangesHaveBeenMadeAlert())
            } else {
                if (state.editedPlayerUrl.isNotEmpty()) {
                    updateUserInFirebase(state = state, imageUrl = state.editedPlayerUrl)
                } else {
                    checkImageUri(uri = uri, state = state)
                }
            }
        } ?: run {
            checkIfPlayerAlreadyExists(uri = uri, state = state)
        }
    }

    fun onClearImageState() {
        createEditPlayerMutableStateFlow.value = createEditPlayerStateFlow.value.copy(editedPlayerUrl = "")
    }

    internal fun hasNotEditedExistingPlayer(existingPlayer: Player, uri: Uri?, state: CreateEditPlayerState): Boolean {
        val hasSameName = existingPlayer.firstName == state.firstName && existingPlayer.lastName == state.lastName
        val hasSamePosition = application.getString(existingPlayer.position.toType()) == state.playerPositionString
        val hasSamePlacedImage = if (existingPlayer.imageUrl == null) {
            false
        } else if (uri != null) {
            false
        } else {
            state.editedPlayerUrl == existingPlayer.imageUrl
        }
        return hasSameName && hasSamePosition && hasSamePlacedImage
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
                            determineToUpdateOrCreateUserInFirebase(state = state, imageUrl = imageUrl)
                        } else {
                            navigation.disableProgress()
                            navigation.alert(alert = notAbleToUploadImageAlert())
                        }
                    }
            } ?: run {
                determineToUpdateOrCreateUserInFirebase(state = state, imageUrl = null)
            }
        }
    }

    suspend fun determineToUpdateOrCreateUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
        if (editedPlayer != null) {
            updateUserInFirebase(state = state, imageUrl = imageUrl)
        } else {
            createUserInFirebase(state = state, imageUrl = imageUrl)
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
                    positionValue = state.playerPositionString.toPlayerPosition(application = application).value,
                    imageUrl = imageUrl ?: ""
                )
            ).collectLatest { isSuccessful ->
                handleFirebaseResponseForSavingPlayer(
                    isSuccessful = isSuccessful,
                    key = key,
                    state = state,
                    imageUrl = imageUrl
                )
            }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    internal fun updateUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
        scope.launch {
            editedPlayer?.let { player ->
                val key = activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey ?: ""
                val playerKey =
                    safeLet(player.firstName, player.lastName) { firstName, lastName ->
                        playerRepository.fetchPlayerByName(firstName = firstName, lastName = lastName)?.firebaseKey ?: ""
                    } ?: run { "" }

                if (key.isNotEmpty() && playerKey.isNotEmpty()) {
                    updateFirebaseUserInfo.updatePlayer(
                        accountKey = key,
                        playerInfoRealtimeWithKeyResponse = PlayerInfoRealtimeWithKeyResponse(
                            playerFirebaseKey = playerKey,
                            playerInfo = PlayerInfoRealtimeResponse(
                                firstName = state.firstName,
                                lastName = state.lastName,
                                positionValue = state.playerPositionString.toPlayerPosition(
                                    application = application
                                ).value,
                                imageUrl = imageUrl ?: ""
                            )
                        )
                    ).collectLatest { isSuccessful ->
                        handleFirebaseResponseForSavingPlayer(
                            isSuccessful = isSuccessful,
                            key = key,
                            state = state,
                            imageUrl = imageUrl
                        )
                    }
                } else {
                    navigation.disableProgress()
                    navigation.alert(alert = weHaveDetectedAProblemWithYourAccountAlert())
                }
            } ?: run {
                navigation.disableProgress()
                navigation.alert(alert = weHaveDetectedAProblemWithYourAccountAlert())
            }
        }
    }

    suspend fun handleFirebaseResponseForSavingPlayer(
        isSuccessful: Boolean,
        key: String,
        state: CreateEditPlayerState,
        imageUrl: String?
    ) {
        if (isSuccessful) {
            readFirebaseUserInfo.getPlayerInfoList(key)
                .collectLatest { playerInfoRealtimeWithKeyResponseList ->
                    updatePlayerInstance(
                        playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                        state = state,
                        imageUrl = imageUrl
                    )
                }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = weWereNotAbleToCreateThePlayerAlert())
        }
    }

    suspend fun updatePlayerInstance(
        playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse>,
        state: CreateEditPlayerState,
        imageUrl: String?
    ) {
        if (playerInfoRealtimeWithKeyResponseList.isNotEmpty()) {
            handleSavingPlayer(
                playerInfoRealtimeWithKeyResponseList = playerInfoRealtimeWithKeyResponseList,
                state = state,
                imageUrl = imageUrl
            )
        } else {
            navigation.disableProgress()
            navigation.alert(alert = yourPlayerCouldNotBeRetrievedAlert())
        }
    }

    suspend fun handleSavingPlayer(
        playerInfoRealtimeWithKeyResponseList: List<PlayerInfoRealtimeWithKeyResponse>,
        state: CreateEditPlayerState,
        imageUrl: String?
    ) {
        var recentlySavedPlayer: PlayerInfoRealtimeWithKeyResponse? = null
        playerInfoRealtimeWithKeyResponseList.map { player ->
            if (player.playerInfo.firstName == state.firstName && player.playerInfo.lastName == state.lastName) {
                recentlySavedPlayer = player
            }
        }
        recentlySavedPlayer?.let { response ->
            val playerKey = response.playerFirebaseKey
            val positionString = state.playerPositionString.ifEmpty {
                application.getString(StringsIds.pointGuard)
            }
            val player = Player(
                firstName = state.firstName,
                lastName = state.lastName,
                position = positionString.toPlayerPosition(application = application),
                firebaseKey = playerKey,
                imageUrl = imageUrl ?: "",
                shotsLoggedList = emptyList() // todo -> add logic from list state
            )

            createOrEditPlayerInRoom(player = player)

            navigation.disableProgress()
            navigation.pop()
        } ?: run {
            navigation.disableProgress()
            navigation.alert(alert = yourPlayerCouldNotBeRetrievedAlert())
        }
    }

    suspend fun createOrEditPlayerInRoom(player: Player) {
        if (editedPlayer == null) {
            playerRepository.createPlayer(player = player)
        } else {
            editedPlayer?.let { currentPlayer ->
                playerRepository.updatePlayer(
                    currentPlayer = currentPlayer,
                    newPlayer = player
                )
            }
        }
        playersAdditionUpdates.updateNewPlayerHasBeenAddedSharedFlow(hasBeenAdded = true)
    }

    fun onFirstNameValueChanged(newFirstName: String) {
        val currentLastName = createEditPlayerMutableStateFlow.value.lastName

        createEditPlayerMutableStateFlow.value = createEditPlayerMutableStateFlow.value.copy(
            firstName = newFirstName,
            hintLogNewShotText = hintLogNewShotText(
                firstName = newFirstName,
                lastName = currentLastName
            )
        )
    }

    fun onLastNameValueChanged(newLastName: String) {
        val currentFirstName = createEditPlayerMutableStateFlow.value.firstName

        createEditPlayerMutableStateFlow.value = createEditPlayerMutableStateFlow.value.copy(
            lastName = newLastName,
            hintLogNewShotText = hintLogNewShotText(
                firstName = currentFirstName,
                lastName = newLastName
            )
        )
    }

    fun onPlayerPositionStringChanged(newPositionString: String) {
        createEditPlayerMutableStateFlow.value = createEditPlayerMutableStateFlow.value.copy(playerPositionString = newPositionString)
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

    internal fun noChangesHaveBeenMadeAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noChangesMade),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.currentPlayerHasNoChangesDescription)
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

    internal fun removeImageSheet(): Sheet {
        return Sheet(
            title = application.getString(StringsIds.chooseOption),
            values = listOf(application.getString(StringsIds.removeImage))
        )
    }

    internal fun chooseFromGalleryOrTakePictureSheet(): Sheet {
        return Sheet(
            title = application.getString(StringsIds.chooseOption),
            values = listOf(
                application.getString(StringsIds.chooseImageFromGallery),
                application.getString(StringsIds.takeAPicture)
            )
        )
    }

    fun onLogShotsClicked() = navigation.navigateToSelectShot()
}

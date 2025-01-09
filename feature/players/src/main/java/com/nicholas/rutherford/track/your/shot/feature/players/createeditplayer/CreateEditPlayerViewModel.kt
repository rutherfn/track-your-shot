package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.Center.toPlayerPosition
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.feature.players.PlayersAdditionUpdates
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.read.ReadFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.shouldAskForReadMediaImages
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import com.nicholas.rutherford.track.your.shot.helper.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val RESET_SCREEN_DELAY_IN_MILLIS = 500L

class CreateEditPlayerViewModel(
    private val application: Application,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val readFirebaseUserInfo: ReadFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val scope: CoroutineScope,
    private val navigation: CreateEditPlayerNavigation,
    private val playersAdditionUpdates: PlayersAdditionUpdates,
    private val currentPendingShot: CurrentPendingShot,
    private val network: Network
) : ViewModel() {

    internal val createEditPlayerMutableStateFlow =
        MutableStateFlow(value = CreateEditPlayerState())
    val createEditPlayerStateFlow = createEditPlayerMutableStateFlow.asStateFlow()

    internal var editedPlayer: Player? = null
    internal var pendingPlayers: List<Player> = emptyList()

    internal var pendingShotLoggedList: List<PendingShot> = emptyList()

    internal var hasCheckedForExistingPlayer = false

    init {
        scope.launch { collectPendingShotsLogged() }
        scope.launch { collectHasDeletedShotFlow() }
    }

    internal suspend fun collectPendingShotsLogged() {
        currentPendingShot.shotsStateFlow
            .collectLatest { shotLoggedList ->
                processPendingShots(shotLoggedList = shotLoggedList)
            }
    }

    internal suspend fun collectHasDeletedShotFlow() {
        deleteFirebaseUserInfo.hasDeletedShotFlow
            .collectLatest { hasDeletedShot ->
                processHasDeletedShot(hasDeletedShot = hasDeletedShot)
            }
    }

    private fun processPendingShots(shotLoggedList: List<PendingShot>) {
        if (shotLoggedList.isNotEmpty()) {
            pendingShotLoggedList = shotLoggedList

            createEditPlayerMutableStateFlow.update { state ->
                state.copy(
                    pendingShots = pendingShotLoggedList.map { it.shotLogged },
                    shots = currentShotsNotPending()
                )
            }

            navigation.alert(alert = showUpdatedAlert())
        }
    }

    private suspend fun processHasDeletedShot(hasDeletedShot: Boolean) {
        if (hasDeletedShot) {
            val firstName = editedPlayer?.firstName ?: ""
            val lastName = editedPlayer?.lastName ?: ""

            playerRepository.fetchPlayerByName(
                firstName = firstName,
                lastName = lastName
            )
                ?.let { player ->
                    editedPlayer = player
                    createEditPlayerMutableStateFlow.value =
                        createEditPlayerMutableStateFlow.value.copy(
                            firstName = player.firstName,
                            lastName = player.lastName,
                            editedPlayerUrl = player.imageUrl ?: "",
                            toolbarNameResId = StringsIds.editPlayer,
                            playerPositionString = application.getString(player.position.toType()),
                            hintLogNewShotText = hintLogNewShotText(
                                firstName = player.firstName,
                                lastName = player.lastName
                            ),
                            shots = player.shotsLoggedList
                        )
                    deleteFirebaseUserInfo.updateHasDeletedShotFlow(hasDeletedShot = false)
                }
        }
    }

    internal fun showUpdatedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotUpdated),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            ),
            description = application.getString(StringsIds.currentShotHasBeenUpdatedDescription)
        )
    }

    internal fun currentShotsNotPending(): List<ShotLogged> {
        val currentShotsArrayList: ArrayList<ShotLogged> = arrayListOf()

        editedPlayer?.let { player ->
            val pendingShotIds = pendingShotLoggedList.map { it.shotLogged.id }

            player.shotsLoggedList.forEach { shot ->
                if (!pendingShotIds.contains(shot.id)) {
                    currentShotsArrayList.add(shot)
                }
            }
        }

        return currentShotsArrayList.toList()
    }

    fun checkForExistingPlayer(firstNameArgument: String?, lastNameArgument: String?) {
        if (!hasCheckedForExistingPlayer) {
            scope.launch {
                safeLet(firstNameArgument, lastNameArgument) { firstName, lastName ->
                    if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                        playerRepository.fetchPlayerByName(
                            firstName = firstName,
                            lastName = lastName
                        )
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

            hasCheckedForExistingPlayer = true
        }
    }

    internal fun hintLogNewShotText(firstName: String? = null, lastName: String? = null): String {
        var hintLogNewShotText: String = createEditPlayerMutableStateFlow.value.hintLogNewShotText

        safeLet(firstName, lastName) { first, last ->
            hintLogNewShotText = if (first.isNotEmpty() && last.isNotEmpty()) {
                application.getString(StringsIds.hintLogNewShotsForPlayer) + " $first $last"
            } else if (first.isNotEmpty()) {
                application.getString(StringsIds.hintLogNewShotsForPlayer) + " $first"
            } else if (last.isNotEmpty()) {
                application.getString(StringsIds.hintLogNewShotsForPlayer) + " $last"
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
                hintLogNewShotText = hintLogNewShotText(
                    firstName = player.firstName,
                    lastName = player.lastName
                ),
                shots = player.shotsLoggedList
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

    private fun resetState() {
        scope.launch {
            delay(RESET_SCREEN_DELAY_IN_MILLIS)
            clearLocalDeclarations()
            clearState()
        }
    }

    fun onToolbarMenuClicked() {
        if (pendingPlayers.size == Constants.PENDING_PLAYERS_EXPECTED_SIZE || pendingShotLoggedList.isNotEmpty()) {
            navigation.alert(alert = unsavedPlayerChangesAlert())
        } else {
            navigation.pop()
            resetState()
        }
    }

    internal fun clearState() {
        if (editedPlayer == null) {
            createEditPlayerMutableStateFlow.update { state ->
                state.copy(
                    firstName = "",
                    lastName = "",
                    editedPlayerUrl = "",
                    toolbarNameResId = StringsIds.createPlayer,
                    playerPositionString = "",
                    hintLogNewShotText = "",
                    pendingShots = emptyList(),
                    shots = emptyList(),
                    sheet = null
                )
            }
        } else {
            createEditPlayerMutableStateFlow.update { state ->
                state.copy(
                    firstName = "",
                    lastName = "",
                    editedPlayerUrl = "",
                    toolbarNameResId = StringsIds.editPlayer,
                    playerPositionString = "",
                    hintLogNewShotText = "",
                    pendingShots = emptyList(),
                    shots = emptyList(),
                    sheet = null
                )
            }
        }
    }

    internal fun clearLocalDeclarations() {
        currentPendingShot.clearShotList()
        pendingPlayers = emptyList()
        pendingShotLoggedList = emptyList()
        editedPlayer = null
        hasCheckedForExistingPlayer = false
    }

    fun onImageUploadClicked(uri: Uri?) {
        when {
            editedPlayer != null && createEditPlayerMutableStateFlow.value.editedPlayerUrl.isNotEmpty() ->
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
            application.getString(StringsIds.chooseImageFromGallery) -> {
                CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY
            }

            application.getString(StringsIds.takeAPicture) -> {
                CreateEditImageOption.TAKE_A_PICTURE
            }

            application.getString(StringsIds.removeImage) -> {
                CreateEditImageOption.REMOVE_IMAGE
            }

            else -> {
                CreateEditImageOption.CANCEL
            }
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
        createEditPlayerMutableStateFlow.value =
            createEditPlayerMutableStateFlow.value.copy(editedPlayerUrl = "")
    }

    internal fun hasNotEditedExistingPlayer(
        existingPlayer: Player,
        uri: Uri?,
        state: CreateEditPlayerState
    ): Boolean {
        val hasSameName =
            existingPlayer.firstName == state.firstName && existingPlayer.lastName == state.lastName
        val hasSamePosition =
            application.getString(existingPlayer.position.toType()) == state.playerPositionString
        val hasSamePlacedImage = if (existingPlayer.imageUrl == null) {
            false
        } else if (uri != null) {
            false
        } else {
            state.editedPlayerUrl == existingPlayer.imageUrl
        }
        return hasSameName && hasSamePosition && hasSamePlacedImage && pendingShotLoggedList.isEmpty()
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
                            determineToUpdateOrCreateUserInFirebase(
                                state = state,
                                imageUrl = imageUrl
                            )
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

    suspend fun determineToUpdateOrCreateUserInFirebase(
        state: CreateEditPlayerState,
        imageUrl: String?
    ) {
        if (editedPlayer != null) {
            updateUserInFirebase(state = state, imageUrl = imageUrl)
        } else {
            createUserInFirebase(state = state, imageUrl = imageUrl)
        }
    }

    suspend fun createUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
        createFirebaseUserInfo.attemptToCreatePlayerFirebaseRealtimeDatabaseResponseFlow(
            playerInfoRealtimeResponse = PlayerInfoRealtimeResponse(
                firstName = state.firstName,
                lastName = state.lastName,
                positionValue = state.playerPositionString.toPlayerPosition(application = application).value,
                imageUrl = imageUrl ?: "",
                shotsLogged = currentShotLoggedRealtimeResponseList(currentShotList = state.shots.map { shots -> shots.toRealtimeResponse() })
            )
        ).collectLatest { result ->
            result.second?.let { key ->
                val isSuccessful = result.first
                if (isSuccessful && key.isNotEmpty()) {
                    handleSavingPlayer(
                        key = key,
                        state = state,
                        imageUrl = imageUrl
                    )
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

    internal fun currentShotLoggedRealtimeResponseList(currentShotList: List<ShotLoggedRealtimeResponse>): List<ShotLoggedRealtimeResponse> {
        if (pendingShotLoggedList.isNotEmpty()) {
            val shotLoggedRealtimeResponseArrayList: ArrayList<ShotLoggedRealtimeResponse> = arrayListOf()

            pendingShotLoggedList.forEach { pendingShot ->
                shotLoggedRealtimeResponseArrayList.add(
                    ShotLoggedRealtimeResponse(
                        id = pendingShot.shotLogged.id,
                        shotName = pendingShot.shotLogged.shotName,
                        shotType = pendingShot.shotLogged.shotType,
                        shotsAttempted = pendingShot.shotLogged.shotsAttempted,
                        shotsMade = pendingShot.shotLogged.shotsMade,
                        shotsMissed = pendingShot.shotLogged.shotsMissed,
                        shotsMadePercentValue = pendingShot.shotLogged.shotsMadePercentValue,
                        shotsMissedPercentValue = pendingShot.shotLogged.shotsMissedPercentValue,
                        shotsAttemptedMillisecondsValue = pendingShot.shotLogged.shotsAttemptedMillisecondsValue,
                        shotsLoggedMillisecondsValue = pendingShot.shotLogged.shotsLoggedMillisecondsValue,
                        isPending = false
                    )
                )
            }
            return currentShotList + shotLoggedRealtimeResponseArrayList.toList()
        } else {
            return currentShotList
        }
    }

    internal fun currentShotLoggedList(currentShotLoggedList: List<ShotLogged>): List<ShotLogged> {
        return if (pendingShotLoggedList.isNotEmpty()) {
            currentShotLoggedList + pendingShotLoggedList.map { pendingShot -> pendingShot.shotLogged.copy(isPending = false) }
        } else {
            currentShotLoggedList
        }
    }

    internal fun updateUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
        scope.launch {
            editedPlayer?.let { player ->
                val key = activeUserRepository.fetchActiveUser()?.firebaseAccountInfoKey ?: ""
                val playerKey =
                    safeLet(player.firstName, player.lastName) { firstName, lastName ->
                        playerRepository.fetchPlayerByName(
                            firstName = firstName,
                            lastName = lastName
                        )?.firebaseKey ?: ""
                    } ?: run { "" }

                if (key.isNotEmpty() && playerKey.isNotEmpty()) {
                    updateFirebaseUserInfo.updatePlayer(
                        playerInfoRealtimeWithKeyResponse = PlayerInfoRealtimeWithKeyResponse(
                            playerFirebaseKey = playerKey,
                            playerInfo = PlayerInfoRealtimeResponse(
                                firstName = state.firstName,
                                lastName = state.lastName,
                                positionValue = state.playerPositionString.toPlayerPosition(
                                    application = application
                                ).value,
                                imageUrl = imageUrl ?: "",
                                shotsLogged = currentShotLoggedRealtimeResponseList(currentShotList = state.shots.map { shots -> shots.toRealtimeResponse() })
                            )
                        )
                    ).collectLatest { isSuccessful ->
                        handleSavingPlayer(
                            key = playerKey,
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

    suspend fun handleSavingPlayer(
        key: String,
        state: CreateEditPlayerState,
        imageUrl: String?
    ) {
            val positionString = state.playerPositionString.ifEmpty {
                application.getString(StringsIds.pointGuard)
            }
            val player = Player(
                firstName = state.firstName,
                lastName = state.lastName,
                position = positionString.toPlayerPosition(application = application),
                firebaseKey = key,
                imageUrl = imageUrl ?: "",
                shotsLoggedList = currentShotLoggedList(currentShotLoggedList = state.shots)
            )

            createOrEditPlayerInRoom(player = player)

            val allPlayers = playerRepository.fetchAllPlayers()
            println("here are all the players $allPlayers")

            currentPendingShot.clearShotList()

            navigation.disableProgress()
            navigation.pop()

            resetState()
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
        createEditPlayerMutableStateFlow.value =
            createEditPlayerMutableStateFlow.value.copy(playerPositionString = newPositionString)
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
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }

    internal fun notAbleToUploadImageAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToUploadImage),
            description = application.getString(StringsIds.theImageUploadWasUnsuccessful),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.ok
                )
            )
        )
    }

    internal fun weHaveDetectedAProblemWithYourAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }

    internal fun weWereNotAbleToCreateThePlayerAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerCreationFailedPleaseTryAgain),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }

    internal fun yourPlayerCouldNotBeRetrievedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.yourPlayerCouldNotBeRetrievedDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }

    internal fun playerAlreadyHasBeenAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerAlreadyHasBeenAddedDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(
                    StringsIds.gotIt
                )
            )
        )
    }

    internal fun unsavedPlayerChangesAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unsavedPlayerChanges),
            description = application.getString(StringsIds.doYouWishToProceedDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { onConfirmUnsavedPlayerChangesButtonClicked() }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no),
                onButtonClicked = {}
            )
        )
    }

    fun onConfirmUnsavedPlayerChangesButtonClicked() {
        editedPlayer = null
        if (pendingPlayers.size == Constants.PENDING_PLAYERS_EXPECTED_SIZE) {
            scope.launch { pendingPlayerRepository.deleteAllPendingPlayers() }
            pendingPlayers = emptyList()
        }
        navigation.pop()
        scope.launch {
            delay(RESET_SCREEN_DELAY_IN_MILLIS)
            currentPendingShot.clearShotList()
            clearState()
            clearLocalDeclarations()
            pendingShotLoggedList = emptyList()
        }
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

    /**
     * Checks if the user has access to log shots.
     * Returns true if the user is an edited player or if the required information for creating a new player is provided.
     * Otherwise, shows an alert and returns false.
     */
    internal fun hasLogShotsAccess(): Boolean {
        // If an edited player exists, return true
        editedPlayer?.let {
            return true
        }

        // If creating a new player, validate first and last names
        val firstName = createEditPlayerMutableStateFlow.value.firstName
        val lastName = createEditPlayerMutableStateFlow.value.lastName

        return when {
            firstName.isEmpty() -> {
                // Show alert for empty first name
                navigation.alert(alert = firstNameEmptyAlert())
                false
            }
            lastName.isEmpty() -> {
                // Show alert for empty last name
                navigation.alert(alert = lastNameEmptyAlert())
                false
            }
            else -> true
        }
    }

    /**
     * Retrieves the ID of an existing player or a pending player.
     * If the device is not connected to the internet, shows an alert and returns null.
     * If an edited player exists, fetches the ID from the repository.
     * Otherwise, creates a pending player and returns its ID.
     */
    internal suspend fun existingOrPendingPlayerId(): Int? {
        // Check if the device is connected to the internet
        if (!network.isDeviceConnectedToInternet()) {
            // Show alert for not connected to the internet
            navigation.alert(alert = notConnectedToInternetAlert())
            return null
        } else {
            // Check if an edited player exists
            editedPlayer?.let { player ->
                // Fetch ID of the existing player and return it
                return playerRepository.fetchPlayerIdByName(
                    firstName = player.firstName,
                    lastName = player.lastName
                )
            } ?: run {
                // Delete any pending players if they exist
                pendingPlayerRepository.fetchAllPendingPlayers().takeIf { it.isNotEmpty() }?.let {
                    pendingPlayers = emptyList()
                    pendingPlayerRepository.deleteAllPendingPlayers()
                }

                // Create a new pending player
                val firstName = createEditPlayerMutableStateFlow.value.firstName
                val lastName = createEditPlayerMutableStateFlow.value.lastName
                val pendingPlayer = Player(
                    firstName = firstName,
                    lastName = lastName,
                    position = createEditPlayerMutableStateFlow.value.playerPositionString.toPlayerPosition(
                        application = application
                    ),
                    firebaseKey = "",
                    imageUrl = "",
                    shotsLoggedList = emptyList()
                )
                pendingPlayerRepository.createPendingPlayer(player = pendingPlayer)

                pendingPlayers = listOf(pendingPlayer)

                // Fetch ID of the pending player and return it
                return pendingPlayerRepository.fetchPendingPlayerIdByName(
                    firstName = firstName,
                    lastName = lastName
                )
            }
        }
    }

    fun onLogShotsClicked() {
        if (hasLogShotsAccess()) {
            scope.launch {
                existingOrPendingPlayerId()?.let { playerId ->
                    navigation.navigateToSelectShot(
                        isExistingPlayer = editedPlayer != null,
                        playerId = playerId
                    )
                }
            }
        }
    }

    fun onViewPendingShotClicked(shotType: Int, shotId: Int) {
        scope.launch {
            navigation.navigateToLogShot(
                isExistingPlayer = editedPlayer != null,
                playerId = existingOrPendingPlayerId() ?: 0,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = false,
                viewCurrentPendingShot = true
            )
        }
    }

    fun onViewShotClicked(shotType: Int, shotId: Int) {
        scope.launch {
            navigation.navigateToLogShot(
                isExistingPlayer = true,
                playerId = existingOrPendingPlayerId() ?: 0,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = true,
                viewCurrentPendingShot = false
            )
        }
    }

    private fun ShotLogged.toRealtimeResponse(): ShotLoggedRealtimeResponse {
        return ShotLoggedRealtimeResponse(
            id = this.id,
            shotName = this.shotName,
            shotType = this.shotType,
            shotsAttempted = this.shotsAttempted,
            shotsMade = this.shotsMade,
            shotsMissed = this.shotsMissed,
            shotsMadePercentValue = this.shotsMadePercentValue,
            shotsMissedPercentValue = this.shotsMissedPercentValue,
            shotsAttemptedMillisecondsValue = this.shotsAttemptedMillisecondsValue,
            shotsLoggedMillisecondsValue = this.shotsLoggedMillisecondsValue,
            isPending = this.isPending
        )
    }
}

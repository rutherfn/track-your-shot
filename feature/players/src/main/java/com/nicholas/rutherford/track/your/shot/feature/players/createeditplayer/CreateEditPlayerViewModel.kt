package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
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
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val RESET_SCREEN_DELAY_IN_MILLIS = 500L

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * ViewModel responsible for handling the create and edit player screen logic.
 *
 * Manages state for player details, image uploads, and shot logging.
 * Handles interaction with repositories for player and pending shot data,
 * Firebase user info creation/updating, and navigation commands.
 *
 * @param savedStateHandle Provides saved state access to retrieve initial player parameters.
 * @param application Application context used for resource access.
 * @param deleteFirebaseUserInfo Use case to delete user info in Firebase.
 * @param createFirebaseUserInfo Use case to create user info in Firebase.
 * @param updateFirebaseUserInfo Use case to update user info in Firebase.
 * @param playerRepository Repository to access and update player data locally.
 * @param pendingPlayerRepository Repository to access pending players.
 * @param activeUserRepository Repository to access active user data.
 * @param scope CoroutineScope for launching asynchronous tasks.
 * @param navigation Interface to perform navigation and UI alert actions.
 * @param currentPendingShot Tracks the current pending shots logged.
 */
class CreateEditPlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val scope: CoroutineScope,
    private val navigation: CreateEditPlayerNavigation,
    private val currentPendingShot: CurrentPendingShot
) : BaseViewModel() {

    /**
     * Mutable state flow holding the UI state for create/edit player screen.
     */
    internal val createEditPlayerMutableStateFlow =
        MutableStateFlow(value = CreateEditPlayerState())
    val createEditPlayerStateFlow = createEditPlayerMutableStateFlow.asStateFlow()

    /** The player currently being edited, if any. */
    internal var editedPlayer: Player? = null

    /** List of pending players to be handled. */
    internal var pendingPlayers: List<Player> = emptyList()

    /** List of shots that are logged but pending confirmation. */
    internal var pendingShotLoggedList: List<PendingShot> = emptyList()

    /** Flag indicating whether existing player check has been performed. */
    internal var hasCheckedForExistingPlayer = false

    /** Initial first name from saved state, if any. */
    internal val firstNameParam: String = savedStateHandle.get<String>("firstName") ?: ""

    /** Initial last name from saved state, if any. */
    internal val lastNameParam: String = savedStateHandle.get<String>("lastName") ?: ""

    init {
        checkForExistingPlayer(firstName = firstNameParam, lastName = lastNameParam)
        scope.launch { collectPendingShotsLogged() }
        scope.launch { collectHasDeletedShotFlow() }
    }

    /**
     * Updates the image URI state in the UI.
     *
     * @param uri The new image URI or null to clear.
     */
    fun updateImageUriState(uri: Uri?) =
        createEditPlayerMutableStateFlow.update { state -> state.copy(imageUri = uri) }

    /**
     * Collects pending shots logged and updates state accordingly.
     */
    internal suspend fun collectPendingShotsLogged() {
        currentPendingShot.shotsStateFlow
            .collectLatest { shotLoggedList ->
                processPendingShots(shotLoggedList = shotLoggedList)
            }
    }

    /**
     * Collects changes to the deleted shot flow and processes UI updates.
     */
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

    /**
     * Creates an Alert to inform user a shot was updated.
     */
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

    /**
     * Returns a list of shots that are not pending from the current edited player.
     */
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

    /**
     * Checks if a player already exists in the repository by first and last name,
     * then updates UI state accordingly.
     */
    fun checkForExistingPlayer(firstName: String, lastName: String) {
        scope.launch {
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
        }
    }

    /**
     * Generates the hint text for logging a new shot based on the player's name.
     *
     * @param firstName Optional first name.
     * @param lastName Optional last name.
     * @return The hint text to show.
     */
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

    /**
     * Updates the UI state for an existing player loaded from the repository.
     */
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

    /**
     * Updates the toolbar title and hint text to "Create Player" state.
     */
    fun updateToolbarNameResIdStateToCreatePlayer() {
        createEditPlayerMutableStateFlow.update { state ->
            state.copy(
                toolbarNameResId = StringsIds.createPlayer,
                hintLogNewShotText = hintLogNewShotText(firstName = null, lastName = null)
            )
        }
    }

    /**
     * Handles the toolbar menu click.
     * If there are unsaved changes, shows an alert.
     * Otherwise, pops the screen and resets state.
     */
    fun onToolbarMenuClicked() {
        if (pendingPlayers.size == Constants.PENDING_PLAYERS_EXPECTED_SIZE || pendingShotLoggedList.isNotEmpty()) {
            navigation.alert(alert = unsavedPlayerChangesAlert())
        } else {
            navigation.pop()
        }
    }

    /**
     * Clears the UI state depending on whether editing an existing player or creating new.
     */
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

    /**
     * Clears local declarations and pending shot lists.
     */
    internal fun clearLocalDeclarations() {
        currentPendingShot.clearShotList()
        pendingPlayers = emptyList()
        pendingShotLoggedList = emptyList()
        editedPlayer = null
    }

    /**
     * Handles the event when user clicks to upload an image.
     * Decides which bottom sheet to show based on current state.
     */
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

    /**
     * Maps the string option selected from the bottom sheet to a [CreateEditImageOption].
     *
     * @param option The string option selected.
     * @return The corresponding [CreateEditImageOption].
     */
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

    /**
     * Navigates to the app settings screen.
     */
    internal fun onNavigateToAppSettings() = navigation.appSettings()

    /**
     * Shows alert if camera permission is not granted.
     */
    fun permissionNotGrantedForCameraAlert() {
        navigation.alert(alert = cameraPermissionNotGrantedAlert())
    }

    /**
     * Handles create player button click.
     * Checks internet connectivity before proceeding.
     *
     * @param isConnectedToInternet Whether internet is available.
     */
    fun onCreatePlayerClicked(isConnectedToInternet: Boolean) {
        scope.launch {
            if (isConnectedToInternet) {
                val state = createEditPlayerMutableStateFlow.value

                navigation.enableProgress(progress = Progress())

                validatePlayer(state = state, uri = state.imageUri)
            } else {
                navigation.alert(alert = notConnectedToInternetAlert())
            }
        }
    }

    /**
     * Validates the player input before creating or editing.
     *
     * @param state The current screen state.
     * @param uri The selected image URI.
     */
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

    /**
     * Determines whether to create a new player or update an existing one.
     *
     * @param state The current screen state.
     * @param uri The selected image URI.
     */
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

    /**
     * Clears the edited player image URL from the state.
     */
    fun onClearImageState() {
        createEditPlayerMutableStateFlow.value =
            createEditPlayerMutableStateFlow.value.copy(editedPlayerUrl = "")
    }

    /**
     * Checks if there were any edits made to the existing player.
     */
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

    /**
     * Checks if a player with the same name already exists before creating.
     */
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

    /**
     * Handles the image URI: uploads the image to Firebase if present,
     * or proceeds to create/update player without image.
     */
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

    private suspend fun determineToUpdateOrCreateUserInFirebase(
        state: CreateEditPlayerState,
        imageUrl: String?
    ) {
        if (editedPlayer != null) {
            updateUserInFirebase(state = state, imageUrl = imageUrl)
        } else {
            createUserInFirebase(state = state, imageUrl = imageUrl)
        }
    }

    private suspend fun createUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
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

    /**
     * Combines current shots with any pending shots (not yet confirmed).
     *
     * @param currentShotList List of confirmed shots.
     * @return Combined list including pending shots marked as not pending.
     */
    internal fun currentShotLoggedRealtimeResponseList(currentShotList: List<ShotLoggedRealtimeResponse>): List<ShotLoggedRealtimeResponse> {
        if (pendingShotLoggedList.isNotEmpty()) {
            val shotLoggedRealtimeResponseArrayList: ArrayList<ShotLoggedRealtimeResponse> =
                arrayListOf()

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

    /**
     * Combines current shots with pending shots (marked as not pending).
     *
     * @param currentShotLoggedList List of confirmed shots.
     * @return Combined list including pending shots.
     */
    internal fun currentShotLoggedList(currentShotLoggedList: List<ShotLogged>): List<ShotLogged> {
        return if (pendingShotLoggedList.isNotEmpty()) {
            currentShotLoggedList + pendingShotLoggedList.map { pendingShot ->
                pendingShot.shotLogged.copy(
                    isPending = false
                )
            }
        } else {
            currentShotLoggedList
        }
    }

    /**
     * Updates an existing user in Firebase with the new state and image URL.
     */
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
                        if (isSuccessful) {
                            handleSavingPlayer(
                                key = playerKey,
                                state = state,
                                imageUrl = imageUrl
                            )
                        }
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

    /**
     * Handles saving player data locally and resets the state.
     */
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

        currentPendingShot.clearShotList()

        navigation.disableProgress()
        navigation.navigateToPlayersList()
    }

    /**
     * Creates or updates player in local database.
     */
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
    }

    /**
     * Updates the first name in the state and refreshes the hint text.
     */
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

    /**
     * Updates the last name value in the mutable state flow and refreshes the hint text accordingly.
     *
     * Retrieves the current first name from the state and uses it along with the new last name
     * to update the hint text displayed for logging a new shot.
     *
     * @param newLastName The new last name entered by the user.
     */
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

    /**
     * Updates the player position string in the mutable state flow.
     *
     * @param newPositionString The new player position string selected or entered by the user.
     */
    fun onPlayerPositionStringChanged(newPositionString: String) {
        createEditPlayerMutableStateFlow.value =
            createEditPlayerMutableStateFlow.value.copy(playerPositionString = newPositionString)
    }

    /**
     * Creates an alert dialog informing the user that camera permission has been declined.
     *
     * Provides options to navigate to app settings or dismiss the alert.
     *
     * @return Alert configured for camera permission denial.
     */
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

    /**
     * Creates an alert dialog informing the user that media or external storage permission has been declined.
     *
     * The description changes depending on whether the permission should be requested again.
     *
     * Provides options to navigate to app settings or dismiss the alert.
     *
     * @param shouldAskForPermission Indicates if permission should be requested again; affects description text.
     * @return Alert configured for media or external storage permission denial.
     */
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

    /**
     * Creates an alert dialog indicating that no first name has been entered.
     *
     * Provides a dismiss button.
     *
     * @return Alert for empty first name input.
     */
    internal fun firstNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noFirstNameEntered),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playersFirstNameEmptyDescription)
        )
    }

    /**
     * Creates an alert dialog indicating that no last name has been entered.
     *
     * Provides a dismiss button.
     *
     * @return Alert for empty last name input.
     */
    internal fun lastNameEmptyAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noLastNameEntered),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.playersLastNameEmptyDescription)
        )
    }

    /**
     * Creates an alert dialog informing the user that no changes have been made to the current player.
     *
     * Provides a dismiss button.
     *
     * @return Alert indicating no changes were detected.
     */
    internal fun noChangesHaveBeenMadeAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.noChangesMade),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            ),
            description = application.getString(StringsIds.currentPlayerHasNoChangesDescription)
        )
    }

    /**
     * Creates an alert dialog informing the user that there is no internet connection.
     *
     * Provides a dismiss button.
     *
     * @return Alert for lack of internet connectivity.
     */
    internal fun notConnectedToInternetAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.notConnectedToInternet),
            description = application.getString(StringsIds.weHaveDetectedCurrentlyNotConnectedToInternetDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert dialog indicating that image upload was unsuccessful.
     *
     * Provides a dismiss button.
     *
     * @return Alert for failed image upload.
     */
    internal fun notAbleToUploadImageAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToUploadImage),
            description = application.getString(StringsIds.theImageUploadWasUnsuccessful),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.ok)
            )
        )
    }

    /**
     * Creates an alert dialog indicating that a problem with the user's account was detected.
     *
     * Provides a dismiss button.
     *
     * @return Alert for detected account issues.
     */
    internal fun weHaveDetectedAProblemWithYourAccountAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.weHaveDetectedAProblemWithYourAccountPleaseContactSupportToResolveIssue),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert dialog indicating that the player creation failed.
     *
     * Provides a dismiss button.
     *
     * @return Alert for failed player creation.
     */
    internal fun weWereNotAbleToCreateThePlayerAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerCreationFailedPleaseTryAgain),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert dialog indicating that the player's data could not be retrieved.
     *
     * Provides a dismiss button.
     *
     * @return Alert for failed player retrieval.
     */
    internal fun yourPlayerCouldNotBeRetrievedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.yourPlayerCouldNotBeRetrievedDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert dialog indicating the player has already been added.
     *
     * Provides a dismiss button.
     *
     * @return Alert for duplicate player addition.
     */
    internal fun playerAlreadyHasBeenAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.issueOccurred),
            description = application.getString(StringsIds.playerAlreadyHasBeenAddedDescription),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Creates an alert dialog asking the user to confirm proceeding with unsaved player changes.
     *
     * Provides buttons to confirm or cancel the action.
     *
     * @return Alert prompting confirmation for unsaved changes.
     */
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

    /**
     * Handles confirmation action when the user chooses to discard unsaved player changes.
     *
     * - Clears the current edited player reference.
     * - If the pending players list has the expected size, deletes all pending players asynchronously.
     * - Navigates back in the UI stack.
     * - After a delay, clears the current pending shots, resets the state, clears local variables,
     *   and empties the pending shot logged list.
     */
    fun onConfirmUnsavedPlayerChangesButtonClicked() {
        editedPlayer = null
        if (pendingPlayers.size == Constants.PENDING_PLAYERS_EXPECTED_SIZE) {
            scope.launch { pendingPlayerRepository.deleteAllPendingPlayers() }
            pendingPlayers = emptyList()
        }
        navigation.navigateToPlayersList()
        scope.launch {
            delay(RESET_SCREEN_DELAY_IN_MILLIS)
            currentPendingShot.clearShotList()
            clearState()
            clearLocalDeclarations()
            pendingShotLoggedList = emptyList()
        }
    }

    /**
     * Creates a [Sheet] UI model representing an option sheet to remove the player image.
     *
     * @return A [Sheet] instance with a title and a single option to remove the image.
     */
    internal fun removeImageSheet(): Sheet {
        return Sheet(
            title = application.getString(StringsIds.chooseOption),
            values = listOf(application.getString(StringsIds.removeImage))
        )
    }

    /**
     * Creates a [Sheet] UI model representing an option sheet for selecting an image source.
     *
     * Provides options to either choose an image from the gallery or take a new picture.
     *
     * @return A [Sheet] instance with a title and two options: choose from gallery or take a picture.
     */
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
            println("get here test")
            return true
        }

        // If creating a new player, validate first and last names
        val firstName = createEditPlayerMutableStateFlow.value.firstName
        val lastName = createEditPlayerMutableStateFlow.value.lastName

        return when {
            firstName.isEmpty() -> {
                println("get here test12121")
                // Show alert for empty first name
                navigation.alert(alert = firstNameEmptyAlert())
                false
            }

            lastName.isEmpty() -> {
                println("get here test1212131212")
                // Show alert for empty last name
                navigation.alert(alert = lastNameEmptyAlert())
                false
            }

            else -> true
        }
    }

    /**
     * Retrieves the ID of an existing player or a pending player.
     * If an edited player exists, fetches the ID from the repository.
     * Otherwise, creates a pending player and returns its ID.
     */
    internal suspend fun existingOrPendingPlayerId(): Int? {
        return editedPlayer?.let { player ->
            playerRepository.fetchPlayerIdByName(
                firstName = player.firstName,
                lastName = player.lastName
            )
        } ?: run {
            pendingPlayers = emptyList()
            pendingPlayerRepository.deleteAllPendingPlayers()

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
            pendingPlayerRepository.fetchPendingPlayerIdByName(
                firstName = firstName,
                lastName = lastName
            )
        }
    }

    /**
     * Handles the event when the "Log Shots" action is triggered.
     *
     * Checks if the user has access to log shots, then launches a coroutine to
     * retrieve the existing or pending player ID and navigates to the shot selection screen.
     */
    fun onLogShotsClicked() {
        if (hasLogShotsAccess()) {
            scope.launch {
                println("gdsdsds")
                existingOrPendingPlayerId()?.let { playerId ->
                    println("get here player id")
                    navigation.navigateToSelectShot(
                        isExistingPlayer = editedPlayer != null,
                        playerId = playerId
                    )
                }
            }
        }
    }

    /**
     * Handles the event when a pending shot is clicked for viewing.
     *
     * Launches a coroutine to navigate to the shot logging screen for the specified pending shot.
     *
     * @param shotType The type of the shot.
     * @param shotId The unique identifier of the shot.
     */
    fun onViewPendingShotClicked(shotType: Int, shotId: Int) {
        scope.launch {
            navigation.navigateToLogShot(
                isExistingPlayer = editedPlayer != null,
                playerId = existingOrPendingPlayerId() ?: 0,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = false,
                viewCurrentPendingShot = true,
                fromShotList = false
            )
        }
    }

    /**
     * Handles the event when an existing shot is clicked for viewing.
     *
     * Launches a coroutine to navigate to the shot logging screen for the specified existing shot.
     *
     * @param shotType The type of the shot.
     * @param shotId The unique identifier of the shot.
     */
    fun onViewShotClicked(shotType: Int, shotId: Int) {
        scope.launch {
            navigation.navigateToLogShot(
                isExistingPlayer = true,
                playerId = existingOrPendingPlayerId() ?: 0,
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = true,
                viewCurrentPendingShot = false,
                fromShotList = false
            )
        }
    }

    /**
     * Extension function to convert a [ShotLogged] instance to a [ShotLoggedRealtimeResponse].
     *
     * Maps all relevant shot properties and preserves the pending state.
     *
     * @receiver The [ShotLogged] instance to convert.
     * @return The corresponding [ShotLoggedRealtimeResponse] instance.
     */
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

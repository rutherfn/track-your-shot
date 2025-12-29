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
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper.CreateEditPlayerSharedHelper
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper.CreatePlayerHelper
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.helper.EditPlayerHelper
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
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
    private val firstNameParam: String = savedStateHandle.get<String>("firstName") ?: ""

    /** Initial last name from saved state, if any. */
    private val lastNameParam: String = savedStateHandle.get<String>("lastName") ?: ""

    /** Helper classes for organizing logic */
    private val sharedHelper = CreateEditPlayerSharedHelper(application = application)
    private val createPlayerHelper = CreatePlayerHelper(
        application = application,
        playerRepository = playerRepository,
        createFirebaseUserInfo = createFirebaseUserInfo
    )
    private val editPlayerHelper = EditPlayerHelper(
        application = application,
        playerRepository = playerRepository,
        activeUserRepository = activeUserRepository,
        updateFirebaseUserInfo = updateFirebaseUserInfo,
        deleteFirebaseUserInfo = deleteFirebaseUserInfo
    )

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
                    shots = sharedHelper.currentShotsNotPending(
                        editedPlayerShots = editedPlayer?.shotsLoggedList ?: emptyList(),
                        pendingShotLoggedList = pendingShotLoggedList
                    )
                )
            }

            navigation.alert(alert = sharedHelper.showUpdatedAlert())
        }
    }

    private suspend fun processHasDeletedShot(hasDeletedShot: Boolean) {
        if (hasDeletedShot) {
            val firstName = editedPlayer?.firstName ?: ""
            val lastName = editedPlayer?.lastName ?: ""

            editPlayerHelper.processHasDeletedShot(
                hasDeletedShot = hasDeletedShot,
                playerFirstName = firstName,
                playerLastName = lastName
            )?.let { player ->
                editedPlayer = player
                val updatedState = editPlayerHelper.buildStateForExistingPlayer(
                    player = player,
                    currentHintText = createEditPlayerMutableStateFlow.value.hintLogNewShotText,
                    sharedHelper = sharedHelper
                )
                createEditPlayerMutableStateFlow.value = updatedState
            }
        }
    }


    /**
     * Checks if a player already exists in the repository by first and last name,
     * then updates UI state accordingly.
     */
    fun checkForExistingPlayer(firstName: String, lastName: String) {
        scope.launch {
            if (firstName.isNotEmpty()) {
                playerRepository.fetchPlayerByName(
                    firstName = firstName,
                    lastName = lastName
                )?.let { player ->
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
        return sharedHelper.hintLogNewShotText(
            firstName = firstName,
            lastName = lastName,
            currentHintText = createEditPlayerMutableStateFlow.value.hintLogNewShotText
        )
    }

    /**
     * Updates the UI state for an existing player loaded from the repository.
     */
    internal fun updateStateForExistingPlayer(player: Player) {
        editedPlayer = player
        val updatedState = editPlayerHelper.buildStateForExistingPlayer(
            player = player,
            currentHintText = createEditPlayerMutableStateFlow.value.hintLogNewShotText,
            sharedHelper = sharedHelper
        )
        createEditPlayerMutableStateFlow.value = updatedState
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
            navigation.alert(alert = sharedHelper.unsavedPlayerChangesAlert {
                onConfirmUnsavedPlayerChangesButtonClicked()
            })
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
            sheet = sharedHelper.chooseFromGalleryOrTakePictureSheet()
        )
    }

    private fun updateSheetToRemoveImageSheet() {
        createEditPlayerMutableStateFlow.value = createEditPlayerMutableStateFlow.value.copy(
            sheet = sharedHelper.removeImageSheet()
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
        navigation.alert(alert = sharedHelper.cameraPermissionNotGrantedAlert {
            onNavigateToAppSettings()
        })
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
                navigation.alert(alert = sharedHelper.notConnectedToInternetAlert())
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
        if (!sharedHelper.validatePlayer(state = state)) {
            navigation.disableProgress()
            navigation.alert(alert = sharedHelper.firstNameEmptyAlert())
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
            if (editPlayerHelper.hasNotEditedExistingPlayer(
                    existingPlayer = player,
                    uri = uri,
                    state = state,
                    pendingShotLoggedList = pendingShotLoggedList
                )
            ) {
                navigation.disableProgress()
                navigation.alert(alert = editPlayerHelper.noChangesHaveBeenMadeAlert())
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
     * Checks if a player with the same name already exists before creating.
     */
    fun checkIfPlayerAlreadyExists(state: CreateEditPlayerState, uri: Uri?) {
        scope.launch {
            val existingPlayer = createPlayerHelper.checkIfPlayerAlreadyExists(state = state)

            if (existingPlayer != null) {
                navigation.disableProgress()
                navigation.alert(alert = sharedHelper.playerAlreadyHasBeenAddedAlert())
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
                createPlayerHelper.uploadImageToFirebase(uri = playerUri)
                    .collectLatest { imageUrl ->
                        if (imageUrl != null) {
                            determineToUpdateOrCreateUserInFirebase(
                                state = state,
                                imageUrl = imageUrl
                            )
                        } else {
                            navigation.disableProgress()
                            navigation.alert(alert = sharedHelper.notAbleToUploadImageAlert())
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
        val shotsRealtimeResponse = state.shots.map { shot ->
            sharedHelper.shotLoggedToRealtimeResponse(shotLogged = shot)
        }
        val combinedShots = sharedHelper.currentShotLoggedRealtimeResponseList(
            currentShotList = shotsRealtimeResponse,
            pendingShotLoggedList = pendingShotLoggedList
        )

        createPlayerHelper.createUserInFirebase(
            state = state,
            imageUrl = imageUrl,
            shotsLoggedRealtimeResponseList = combinedShots
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
                    navigation.alert(alert = sharedHelper.weHaveDetectedAProblemWithYourAccountAlert())
                }
            } ?: run {
                navigation.disableProgress()
                navigation.alert(alert = sharedHelper.weHaveDetectedAProblemWithYourAccountAlert())
            }
        }
    }


    /**
     * Updates an existing user in Firebase with the new state and image URL.
     */
    internal fun updateUserInFirebase(state: CreateEditPlayerState, imageUrl: String?) {
        scope.launch {
            editedPlayer?.let { player ->
                try {
                    val shotsRealtimeResponse = state.shots.map { shot ->
                        sharedHelper.shotLoggedToRealtimeResponse(shotLogged = shot)
                    }
                    val combinedShots = sharedHelper.currentShotLoggedRealtimeResponseList(
                        currentShotList = shotsRealtimeResponse,
                        pendingShotLoggedList = pendingShotLoggedList
                    )

                    editPlayerHelper.updateUserInFirebase(
                        player = player,
                        state = state,
                        imageUrl = imageUrl,
                        shotsLoggedRealtimeResponseList = combinedShots
                    ).collectLatest { isSuccessful ->
                        if (isSuccessful) {
                            handleSavingPlayer(
                                key = player.firebaseKey,
                                state = state,
                                imageUrl = imageUrl
                            )
                        }
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    navigation.disableProgress()
                    navigation.alert(alert = sharedHelper.weHaveDetectedAProblemWithYourAccountAlert())
                }
            } ?: run {
                navigation.disableProgress()
                navigation.alert(alert = sharedHelper.weHaveDetectedAProblemWithYourAccountAlert())
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
        val combinedShots = sharedHelper.currentShotLoggedList(
            currentShotLoggedList = state.shots,
            pendingShotLoggedList = pendingShotLoggedList
        )

        val player = if (editedPlayer == null) {
            createPlayerHelper.buildPlayerFromState(
                state = state,
                firebaseKey = key,
                imageUrl = imageUrl ?: "",
                shotsLoggedList = combinedShots
            )
        } else {
            editPlayerHelper.buildPlayerFromState(
                existingPlayer = editedPlayer!!,
                state = state,
                imageUrl = imageUrl ?: "",
                shotsLoggedList = combinedShots
            )
        }

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
            createPlayerHelper.createPlayerInRoom(player = player)
        } else {
            editPlayerHelper.updatePlayerInRoom(
                currentPlayer = editedPlayer!!,
                newPlayer = player
            )
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
     * Checks if the user has access to log shots.
     * Returns true if the user is an edited player or if the required information for creating a new player is provided.
     * Otherwise, shows an alert and returns false.
     */
    internal fun hasLogShotsAccess(): Boolean {
        // If an edited player exists, return true
        editedPlayer?.let {
            return true
        }

        // If creating a new player, validate first name and make sure its not empty
        val firstName = createEditPlayerMutableStateFlow.value.firstName

        return when {
            firstName.isEmpty() -> {
                // Show alert for empty first name
                navigation.alert(alert = sharedHelper.firstNameEmptyAlert())
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
    internal suspend fun existingOrPendingPlayerId(): Int {
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
                existingOrPendingPlayerId().let { playerId ->
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
                playerId = existingOrPendingPlayerId(),
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
                playerId = existingOrPendingPlayerId(),
                shotType = shotType,
                shotId = shotId,
                viewCurrentExistingShot = true,
                viewCurrentPendingShot = false,
                fromShotList = false
            )
        }
    }

}

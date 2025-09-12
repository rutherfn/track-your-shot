package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.ActiveUserRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PendingPlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotInfo
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension.LogShotViewModelExt
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.CurrentPendingShot
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.pendingshot.PendingShot
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.ShotLoggedRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import com.nicholas.rutherford.track.your.shot.helper.extensions.safeLet
import com.nicholas.rutherford.track.your.shot.helper.extensions.toDateValue
import com.nicholas.rutherford.track.your.shot.helper.extensions.toType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel responsible for managing the state and business logic for logging a basketball shot.
 *
 * Handles fetching existing shot data, updating shot information, saving new or updated shots,
 * deleting shots, and synchronizing shot data with Firebase.
 *
 * @property savedStateHandle Used to retrieve navigation arguments passed to this ViewModel.
 * @property application Provides access to application resources such as strings.
 * @property scope Coroutine scope for launching asynchronous tasks.
 * @property navigation Navigation handler interface for directing UI navigation events.
 * @property declaredShotRepository Repository for fetching declared shot metadata.
 * @property pendingPlayerRepository Repository for fetching pending players (not fully saved players).
 * @property playerRepository Repository for fetching and updating player data.
 * @property activeUserRepository Repository to access the current active user information.
 * @property updateFirebaseUserInfo Use case to update player info and shots in Firebase.
 * @property deleteFirebaseUserInfo Use case to delete shot data from Firebase.
 * @property currentPendingShot Manager for handling pending shot data locally.
 * @property logShotViewModelExt Extension class providing utility functions and alert builders for the ViewModel.
 */
class LogShotViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val scope: CoroutineScope,
    private val navigation: LogShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val pendingPlayerRepository: PendingPlayerRepository,
    private val playerRepository: PlayerRepository,
    private val activeUserRepository: ActiveUserRepository,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val currentPendingShot: CurrentPendingShot,
    private val logShotViewModelExt: LogShotViewModelExt
) : BaseViewModel() {

    val logShotMutableStateFlow = MutableStateFlow(value = LogShotState())
    val logShotStateFlow = logShotMutableStateFlow.asStateFlow()

    internal var currentPlayer: Player? = null

    internal var currentDeclaredShot: DeclaredShot? = null

    private var currentPlayerShotSize = 0

    internal var initialShotLogged: ShotLogged? = null

    private var screenTriggered: ScreenTriggered = ScreenTriggered.UNKNOWN

    private var shouldShowAllPlayersShots: Boolean = false

    /**
     * Navigation arguments extracted from the saved state handle.
     */
    private val isExistingPlayerArgument: Boolean = savedStateHandle.get<Boolean>("isExistingPlayer") ?: false
    private val playerIdArgument: Int = savedStateHandle.get<Int>("playerId") ?: 0
    private val shotTypeArgument: Int = savedStateHandle.get<Int>("shotType") ?: 0
    private val shotIdArgument: Int = savedStateHandle.get<Int>("shotId") ?: 0
    private val viewCurrentExistingShotArgument: Boolean = savedStateHandle.get<Boolean>("viewCurrentExistingShot") ?: false
    private val viewCurrentPendingShotArgument: Boolean = savedStateHandle.get<Boolean>("viewCurrentPendingShot") ?: false
    private val fromShotListArgument: Boolean = savedStateHandle.get<Boolean>("fromShotList") ?: false
    private val screenTriggeredIndexArgument: Int = savedStateHandle.get<Int>("screenTriggeredIndexArgument") ?: ScreenTriggered.INDEX_UNKNOWN

    init {
        updateIsExistingPlayerAndId()
        screenTriggered = ScreenTriggered.fromIndex(screenTriggeredIndexArgument)
        shouldShowAllPlayersShots = when (screenTriggered) {
            ScreenTriggered.FROM_ALL_PLAYERS -> { true }
            ScreenTriggered.FROM_FILTER_PLAYERS -> { false }
            else -> { false }
        }
    }

    /**
     * Initializes and updates the ViewModel state based on the navigation arguments.
     * Loads player and declared shot data asynchronously and updates the UI state.
     */
    fun updateIsExistingPlayerAndId() {
        resetState()

        logShotViewModelExt.setInitialInfo(
            LogShotInfo(
                isExistingPlayer = isExistingPlayerArgument,
                playerId = playerIdArgument,
                shotType = shotTypeArgument,
                shotId = shotIdArgument,
                viewCurrentExistingShot = viewCurrentExistingShotArgument,
                viewCurrentPendingShot = viewCurrentPendingShotArgument,
                fromShotList = fromShotListArgument
            )
        )

        scope.launch {
            val declaredShot = declaredShotRepository.fetchDeclaredShotFromId(id = logShotViewModelExt.logShotInfo.shotType)
            val player = getPlayer(
                isExisting = logShotViewModelExt.logShotInfo.isExistingPlayer,
                playerId = logShotViewModelExt.logShotInfo.playerId
            )

            currentPlayer = player
            currentPlayerShotSize = player?.shotsLoggedList?.size ?: 0
            currentDeclaredShot = declaredShot

            safeLet(declaredShot, player) { shot, existingPlayer ->
                logShotMutableStateFlow.update { state ->
                    state.copy(
                        shotName = shot.title,
                        playerPosition = existingPlayer.position.toType(),
                        playerName = "${existingPlayer.firstName}, ${existingPlayer.lastName}",
                        shotsLoggedDateValue = LocalDate.now().toDateValue() ?: ""
                    )
                }
            }
            updateViewShotState()
        }
    }

    /**
     * Loads and updates the shot state if viewing an existing or pending shot.
     */
    private suspend fun updateViewShotState() {
        logShotViewModelExt.logShotInfo.let { info ->
            if (info.viewCurrentExistingShot) {
                updateShotStateFromExisting()
            }
            if (info.viewCurrentPendingShot) {
                updateShotStateFromPending()
            }
        }
        initializeShotLogged()
    }

    /**
     * Updates the UI state from an existing shot loaded from player data.
     */
    private fun updateShotStateFromExisting() {
        currentPlayer?.shotsLoggedList?.firstOrNull { it.id == logShotViewModelExt.logShotInfo.shotId }?.let { shot ->
            logShotMutableStateFlow.update { state ->
                state.copy(
                    shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                    shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                    shotsMade = shot.shotsMade,
                    shotsMissed = shot.shotsMissed,
                    shotsAttempted = shot.shotsAttempted,
                    shotsMadePercentValue = calculateShotPercentage(shot, isShotsMade = true),
                    shotsMissedPercentValue = calculateShotPercentage(shot, isShotsMade = false),
                    deleteShotButtonVisible = true
                )
            }
        }
    }

    /**
     * Updates the UI state from a pending shot loaded from local pending shot storage.
     */
    private suspend fun updateShotStateFromPending() {
        currentPendingShot.shotsStateFlow.firstOrNull()?.firstOrNull()?.shotLogged?.let { shot ->
            logShotMutableStateFlow.update { state ->
                state.copy(
                    shotsLoggedDateValue = parseDateValueToString(shot.shotsLoggedMillisecondsValue),
                    shotsTakenDateValue = parseDateValueToString(shot.shotsAttemptedMillisecondsValue),
                    shotsMade = shot.shotsMade,
                    shotsMissed = shot.shotsMissed,
                    shotsAttempted = shot.shotsAttempted,
                    shotsMadePercentValue = calculateShotPercentage(shot, isShotsMade = true),
                    shotsMissedPercentValue = calculateShotPercentage(shot, isShotsMade = false),
                    deleteShotButtonVisible = false
                )
            }
        }
    }

    /**
     * Calculates the shot percentage as a formatted string for made or missed shots.
     *
     * @param shot The shot data.
     * @param isShotsMade True to calculate percentage for made shots, false for missed shots.
     * @return Formatted percentage string.
     */
    private fun calculateShotPercentage(shot: ShotLogged, isShotsMade: Boolean): String {
        return logShotViewModelExt.percentageFormat(
            shotsMade = shot.shotsMade.toDouble(),
            shotsMissed = shot.shotsMissed.toDouble(),
            isShotsMade = isShotsMade
        )
    }

    /**
     * Initializes the [initialShotLogged] property with the current state for change detection.
     */
    private fun initializeShotLogged() {
        initialShotLogged = ShotLogged(
            id = 0, // Ignored field
            shotName = logShotMutableStateFlow.value.shotName,
            shotType = currentDeclaredShot?.id ?: 0,
            shotsAttempted = logShotMutableStateFlow.value.shotsAttempted,
            shotsMade = logShotMutableStateFlow.value.shotsMade,
            shotsMissed = logShotMutableStateFlow.value.shotsMissed,
            shotsMadePercentValue = logShotViewModelExt.convertPercentageToDouble(logShotMutableStateFlow.value.shotsMadePercentValue.trim().replace(" ", "")),
            shotsMissedPercentValue = logShotViewModelExt.convertPercentageToDouble(logShotMutableStateFlow.value.shotsMissedPercentValue.trim().replace(" ", "")),
            shotsAttemptedMillisecondsValue = logShotViewModelExt.convertValueToDate(logShotMutableStateFlow.value.shotsTakenDateValue)?.time ?: 0L,
            shotsLoggedMillisecondsValue = logShotViewModelExt.convertValueToDate(logShotMutableStateFlow.value.shotsLoggedDateValue)?.time ?: 0L,
            isPending = true // Ignored field
        )
    }

    /**
     * Fetches a player based on whether they are existing or pending.
     *
     * @param isExisting True if the player is an existing player; false if pending.
     * @param playerId The ID of the player.
     * @return The player or null if not found.
     */
    private suspend fun getPlayer(isExisting: Boolean, playerId: Int): Player? {
        return if (isExisting) {
            playerRepository.fetchPlayerById(id = playerId)
        } else {
            pendingPlayerRepository.fetchPlayerById(id = playerId)
        }
    }

    /**
     * Handler when user clicks to select the date shots were taken.
     * Opens a date picker and updates the UI state when a new date is selected.
     */
    fun onDateShotsTakenClicked() {
        val dateValue = logShotMutableStateFlow.value.shotsTakenDateValue

        val datePickerInfo = DatePickerInfo(
            onDateOkClicked = { newDateValue ->
                logShotMutableStateFlow.update { state ->
                    state.copy(shotsTakenDateValue = newDateValue)
                }
            },
            dateValue = dateValue
        )
        navigation.datePicker(datePickerInfo = datePickerInfo)
    }

    /**
     * Updates the number of shots made and recalculates related UI state values.
     *
     * @param shots New shots made count.
     */
    fun onShotsMadeUpwardOrDownwardClicked(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMade = shots,
                shotsAttempted = logShotViewModelExt.shotsAttempted(shotsMade = shots, shotsMissed = state.shotsMissed),
                shotsMadePercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = shots.toDouble(),
                    shotsMissed = state.shotsMissed.toDouble(),
                    isShotsMade = true
                ),
                shotsMissedPercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = shots.toDouble(),
                    shotsMissed = state.shotsMissed.toDouble(),
                    isShotsMade = false
                )
            )
        }
    }

    /**
     * Updates the number of shots missed and recalculates related UI state values.
     *
     * @param shots New shots missed count.
     */
    fun onShotsMissedUpwardOrDownwardClicked(shots: Int) {
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotsMissed = shots,
                shotsAttempted = logShotViewModelExt.shotsAttempted(shotsMade = state.shotsMade, shotsMissed = shots),
                shotsMadePercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = state.shotsMade.toDouble(),
                    shotsMissed = shots.toDouble(),
                    isShotsMade = true
                ),
                shotsMissedPercentValue = logShotViewModelExt.percentageFormat(
                    shotsMade = state.shotsMade.toDouble(),
                    shotsMissed = shots.toDouble(),
                    isShotsMade = false
                )
            )
        }
    }

    /**
     * Disables any progress UI and shows an alert dialog with the provided alert information.
     *
     * @param alert Alert data to display.
     */
    internal fun disableProgressAndShowAlert(alert: Alert) {
        navigation.disableProgress()
        navigation.alert(alert = alert)
    }

    /**
     * Constructs a [PendingShot] object based on the current player and UI state.
     *
     * @param player The player the shot is associated with.
     * @param state Current UI state containing shot data.
     * @return Constructed [PendingShot].
     */
    private fun buildPendingShotOnSave(player: Player, state: LogShotState): PendingShot =
        PendingShot(
            player = player,
            shotLogged = ShotLogged(
                id = 0,
                shotName = state.shotName,
                shotType = currentDeclaredShot?.id ?: 0,
                shotsAttempted = state.shotsAttempted,
                shotsMade = state.shotsMade,
                shotsMissed = state.shotsMissed,
                shotsMadePercentValue = logShotViewModelExt.convertPercentageToDouble(
                    percentage = state.shotsMadePercentValue.trim().replace(" ", "")
                ),
                shotsMissedPercentValue = logShotViewModelExt.convertPercentageToDouble(
                    percentage = state.shotsMissedPercentValue.trim().replace(" ", "")
                ),
                shotsAttemptedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = state.shotsTakenDateValue)?.time
                    ?: 0L,
                shotsLoggedMillisecondsValue = logShotViewModelExt.convertValueToDate(value = state.shotsLoggedDateValue)?.time
                    ?: 0L,
                isPending = true
            ),
            isPendingPlayer = logShotViewModelExt.logShotInfo.isExistingPlayer
        )

    /**
     * Handles saving a pending shot when editing an existing shot.
     * Checks for changes and either shows an alert or creates/updates the shot.
     *
     * @param pendingShot The shot to save.
     */
    private fun handleExistingShotSaveClicked(pendingShot: PendingShot) {
        logShotViewModelExt.noChangesForShotAlert(initialShotLogged = initialShotLogged, pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
            disableProgressAndShowAlert(alert = alert)
        } ?: createPendingShot(
            isACurrentPlayerShot = true,
            pendingShot = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = logShotViewModelExt.logShotInfo.shotId))
        )
    }

    /**
     * Handles saving a pending shot when editing a pending shot.
     *
     * @param pendingShot The shot to update.
     */
    private fun handlePendingShotSaveClicked(pendingShot: PendingShot) {
        logShotViewModelExt.noChangesForShotAlert(initialShotLogged = initialShotLogged, pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
            disableProgressAndShowAlert(alert = alert)
        } ?: updatePendingShot(pendingShot = pendingShot)
    }

    /**
     * Handles saving a pending shot when saving from the shot list view.
     *
     * @param pendingShot The shot to update.
     */
    private suspend fun handleFromShotListSaveClicked(pendingShot: PendingShot) {
        logShotViewModelExt.noChangesForShotAlert(initialShotLogged = initialShotLogged, pendingShotLogged = pendingShot.shotLogged)?.let { alert ->
            disableProgressAndShowAlert(alert = alert)
        } ?: updateCurrentShot(pendingShot = pendingShot)
    }

    /**
     * Called when the user clicks the save button.
     * Validates input, builds the pending shot, and triggers the appropriate save flow.
     */
    fun onSaveClicked() {
        scope.launch {
            val player = currentPlayer
            if (player == null) {
                navigation.alert(
                    alert = logShotViewModelExt.invalidLogShotAlert(
                        description = application.getString(StringsIds.playerIsInvalidPleaseTryAgain)
                    )
                )
                return@launch
            }

            navigation.enableProgress(Progress())

            val state = logShotMutableStateFlow.value
            val shotDateMillis = logShotViewModelExt.convertValueToDate(value = state.shotsTakenDateValue)?.time ?: 0

            logShotViewModelExt.shotEntryInvalidAlert(
                shotsMade = state.shotsMade,
                shotsMissed = state.shotsMissed,
                shotsAttemptedMillisecondsValue = shotDateMillis
            )?.let { alert ->
                disableProgressAndShowAlert(alert = alert)
                return@launch
            }

            val pendingShot = buildPendingShotOnSave(player = player, state = state)
            val shotInfo = logShotViewModelExt.logShotInfo

            when {
                shotInfo.viewCurrentExistingShot && shotInfo.fromShotList -> handleFromShotListSaveClicked(pendingShot)
                shotInfo.viewCurrentPendingShot -> handlePendingShotSaveClicked(pendingShot)
                shotInfo.viewCurrentExistingShot -> handleExistingShotSaveClicked(pendingShot)
                else -> createPendingShot(isACurrentPlayerShot = false, pendingShot = pendingShot)
            }
        }
    }

    /**
     * Resets the UI state to default empty values.
     */
    private fun resetState() =
        logShotMutableStateFlow.update { state ->
            state.copy(
                shotName = "",
                playerName = "",
                playerPosition = 0,
                shotsLoggedDateValue = "",
                shotsTakenDateValue = "",
                shotsMade = 0,
                shotsMissed = 0,
                shotsAttempted = 0,
                shotsMadePercentValue = "",
                shotsMissedPercentValue = "",
                deleteShotButtonVisible = false
            )
        }

    /**
     * Updates an existing pending shot in the local storage and navigates to the create or edit player screen.
     *
     * @param pendingShot The shot to update.
     */
    private fun updatePendingShot(pendingShot: PendingShot) {
        val firstShotLogged = currentPendingShot.fetchPendingShots().first()
        currentPendingShot.deleteShot(shotLogged = firstShotLogged)
        currentPendingShot.createShot(shotLogged = pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = firstShotLogged.shotLogged.id)))
        navigateToCreateOrEditPlayer()
    }

    /**
     * Updates the user’s player shot info in Firebase and handles the result.
     *
     * @param player The player whose info is being updated.
     * @param shotLogged List of shots to upload.
     */
    private suspend fun updateUserInFirebase(player: Player, shotLogged: List<ShotLogged>) {
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
                        firstName = player.firstName,
                        lastName = player.lastName,
                        positionValue = player.position.value,
                        imageUrl = player.imageUrl ?: "",
                        shotsLogged = currentShotLoggedRealtimeResponseList(currentShotList = shotLogged)
                    )
                )
            ).collectLatest { isSuccessful ->
                handleUpdatedShot(
                    isSuccessful = isSuccessful,
                    player = player,
                    shotLogged = shotLogged
                )
            }
        } else {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    /**
     * Converts a list of [ShotLogged] to a list of Firebase response objects for upload.
     *
     * @param currentShotList List of shots to convert.
     * @return List of [ShotLoggedRealtimeResponse].
     */
    private fun currentShotLoggedRealtimeResponseList(currentShotList: List<ShotLogged>): List<ShotLoggedRealtimeResponse> {
        if (currentShotList.isNotEmpty()) {
            val shotLoggedRealtimeResponseArrayList: ArrayList<ShotLoggedRealtimeResponse> = arrayListOf()

            currentShotList.forEach { shotLogged ->
                shotLoggedRealtimeResponseArrayList.add(
                    ShotLoggedRealtimeResponse(
                        id = shotLogged.id,
                        shotName = shotLogged.shotName,
                        shotType = shotLogged.shotType,
                        shotsAttempted = shotLogged.shotsAttempted,
                        shotsMade = shotLogged.shotsMade,
                        shotsMissed = shotLogged.shotsMissed,
                        shotsMadePercentValue = shotLogged.shotsMadePercentValue,
                        shotsMissedPercentValue = shotLogged.shotsMissedPercentValue,
                        shotsAttemptedMillisecondsValue = shotLogged.shotsAttemptedMillisecondsValue,
                        shotsLoggedMillisecondsValue = shotLogged.shotsLoggedMillisecondsValue,
                        isPending = false
                    )
                )
            }
            return shotLoggedRealtimeResponseArrayList
        } else {
            return arrayListOf()
        }
    }

    /**
     * Handles the result of attempting to update a shot in Firebase.
     *
     * @param isSuccessful Whether the update was successful.
     * @param player Player whose shots were updated.
     * @param shotLogged List of updated shots.
     */
    private suspend fun handleUpdatedShot(
        isSuccessful: Boolean,
        player: Player,
        shotLogged: List<ShotLogged>
    ) {
        if (isSuccessful) {
            playerRepository.updatePlayer(
                currentPlayer = player,
                newPlayer = player.copy(
                    firstName = player.firstName,
                    lastName = player.lastName,
                    position = player.position,
                    firebaseKey = player.firebaseKey,
                    imageUrl = player.imageUrl,
                    shotsLoggedList = shotLogged
                )
            )
            navigation.disableProgress()
            navigation.popToShotList(shouldShowAllPlayersShots = shouldShowAllPlayersShots)
            navigation.alert(alert = logShotViewModelExt.showUpdatedAlert())
        } else {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    /**
     * Updates the current shot data in Firebase by combining existing shots with the pending shot.
     *
     * @param pendingShot Shot data to update.
     */
    private suspend fun updateCurrentShot(pendingShot: PendingShot) {
        currentPlayer?.let { player ->
            updateUserInFirebase(
                player = player,
                shotLogged = logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList) + listOf(pendingShot.shotLogged)
            )
        } ?: run {
            navigation.disableProgress()
            navigation.alert(alert = logShotViewModelExt.weHaveDetectedAProblemWithYourAccountAlert())
        }
    }

    /**
     * Creates a new pending shot entry and navigates to the player creation or editing screen.
     *
     * @param isACurrentPlayerShot True if the shot belongs to an existing player, false if new.
     * @param pendingShot The shot to create.
     */
    internal fun createPendingShot(isACurrentPlayerShot: Boolean, pendingShot: PendingShot) {
        currentPendingShot.createShot(
            shotLogged = if (isACurrentPlayerShot) {
                pendingShot
            } else {
                pendingShot.copy(shotLogged = pendingShot.shotLogged.copy(id = currentPlayerShotSize + 1))
            }
        )
        navigateToCreateOrEditPlayer()
    }

    /**
     * Handles the response from deleting a shot in Firebase.
     *
     * @param hasDeleted True if deletion was successful, false otherwise.
     */
    fun handleHasDeleteShotFirebaseResponse(hasDeleted: Boolean) {
        val shotName = logShotMutableStateFlow.value.shotName

        if (hasDeleted) {
            if (logShotViewModelExt.logShotInfo.fromShotList) {
                navigation.disableProgress()
                navigation.popToShotList(shouldShowAllPlayersShots = shouldShowAllPlayersShots)
            } else {
                navigateToCreateOrEditPlayer()
            }
            navigation.alert(logShotViewModelExt.deleteShotConfirmAlert(shotName))
        } else {
            navigation.disableProgress()
            navigation.alert(logShotViewModelExt.deleteShotErrorAlert(shotName))
        }
    }

    /**
     * Navigates to the player creation or editing screen depending on whether the player exists.
     */
    fun navigateToCreateOrEditPlayer() {
        navigation.disableProgress()
        navigation.popToCreateOrEditPlayer()
    }

    /**
     * Navigates back in the navigation stack.
     */
    fun onBackClicked() = navigation.pop()

    /**
     * Initiates the deletion of the current shot and handles updating data and UI accordingly.
     */
    suspend fun onYesDeleteShot() {
        navigation.enableProgress(progress = Progress())
        currentPlayer?.let { player ->
            playerRepository.updatePlayer(
                currentPlayer = player,
                newPlayer = player.copy(
                    firstName = player.firstName,
                    lastName = player.lastName,
                    position = player.position,
                    firebaseKey = player.firebaseKey,
                    imageUrl = player.imageUrl,
                    shotsLoggedList = logShotViewModelExt.filterShotsById(shots = player.shotsLoggedList)
                )
            )

            deleteFirebaseUserInfo.deleteShot(
                playerKey = player.firebaseKey,
                index = logShotViewModelExt.logShotInfo.shotId - 1
            ).collectLatest { hasDeleted ->
                handleHasDeleteShotFirebaseResponse(hasDeleted = hasDeleted)
            }
        } ?: navigation.disableProgress()
    }

    /**
     * Shows the delete shot confirmation alert dialog.
     */
    fun onDeleteShotClicked() = navigation.alert(
        alert = logShotViewModelExt.deleteShotAlert(
            onYesDeleteShot = { onYesDeleteShot() },
            shotName = logShotMutableStateFlow.value.shotName
        )
    )
}

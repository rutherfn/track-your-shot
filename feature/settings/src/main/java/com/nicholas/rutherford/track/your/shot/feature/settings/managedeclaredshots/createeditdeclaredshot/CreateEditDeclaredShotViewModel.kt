package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.PlayerRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.delete.DeleteFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.core.update.UpdateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.DeclaredShotWithKeyRealtimeResponse
import com.nicholas.rutherford.track.your.shot.helper.extensions.normalizeSpaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel responsible for handling creation, editing, viewing, and deletion of declared shots.
 * Manages UI state, validation, Firebase synchronization, and local persistence.
 *
 * This ViewModel supports:
 * - Loading and caching declared shots.
 * - Validating inputs when creating or editing shots.
 * - Synchronizing declared shots with Firebase Realtime Database.
 * - Handling shot deletion and ignored shots.
 * - Emitting alerts and progress indicators through the [CreateEditDeclaredShotNavigation] interface.
 *
 * @property savedStateHandle Provides access to navigation arguments.
 * @property application Android Application instance for accessing resources.
 * @property declaredShotRepository Repository for local declared shot CRUD operations.
 * @property shotIgnoringRepository Repository for managing ignored shot IDs locally.
 * @property createFirebaseUserInfo Firebase helper for creating declared shots and ignored IDs.
 * @property updateFirebaseUserInfo Firebase helper for updating declared shots.
 * @property deleteFirebaseUserInfo Firebase helper for deleting declared shots.
 * @property navigation Navigation interface to handle screen transitions and UI events.
 * @property scope CoroutineScope for asynchronous tasks.
 */
class CreateEditDeclaredShotViewModel(
    savedStateHandle: SavedStateHandle,
    private val application: Application,
    private val declaredShotRepository: DeclaredShotRepository,
    private val shotIgnoringRepository: ShotIgnoringRepository,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val playerRepository: PlayerRepository,
    private val navigation: CreateEditDeclaredShotNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal var allDeclaredShotNames: List<String> = emptyList()

    internal var currentDeclaredShot: DeclaredShot? = null

    internal var editedDeclaredShot: DeclaredShot? = null

    internal var createdShotInfo = CreateShotInfo()

    internal var createEditDeclaredShotMutableStateFlow = MutableStateFlow(value = CreateEditDeclaredShotState())

    var createEditDeclaredShotStateFlow = createEditDeclaredShotMutableStateFlow.asStateFlow()

    internal val shotNameArgument: String = savedStateHandle.get<String>("shotName") ?: ""

    init {
        scope.launch { initializeDeclaredShotState() }
    }

    /**
     * Initializes the declared shot state by:
     * 1. Preloading all declared shot names for duplicate validation.
     * 2. Restoring the current declared shot from shared preferences if available,
     *    or setting the state to [DeclaredShotState.CREATING] if none is found.
     *
     *  // todo test this
     */
    internal suspend fun initializeDeclaredShotState() {
        allDeclaredShotNames = declaredShotRepository.fetchAllDeclaredShots().map { it.title }

        val name = shotNameArgument
        if (name.isNotEmpty()) {
            attemptToUpdateDeclaredShotState(name = name)
        } else {
            currentDeclaredShot = null
            createEditDeclaredShotMutableStateFlow.update {
                it.copy(
                    declaredShotState = DeclaredShotState.CREATING,
                    currentDeclaredShot = null
                )
            }
        }
    }

    /**
     * Handles toolbar back/menu click.
     * - If in editing mode, returns to viewing mode and resets changes.
     * - Otherwise, resets state and navigates back to declared shot list.
     */
    fun onToolbarMenuClicked() {
        val declaredShotState = createEditDeclaredShotMutableStateFlow.value.declaredShotState
        if (declaredShotState == DeclaredShotState.EDITING) {
            createEditDeclaredShotMutableStateFlow.update {
                it.copy(declaredShotState = DeclaredShotState.VIEWING)
            }
            resetDeclaredShotValues()
        } else {
            resetDeclaredShotValues()
            navigation.navigateToDeclaredShotList()
        }
    }

    /** Resets current and edited declared shot references. */
    private fun resetDeclaredShotValues() {
        currentDeclaredShot = null
        editedDeclaredShot = null
    }

    /**
     * Attempts to load a declared shot by name and update the state to viewing mode.
     *
     * @param name Name of the declared shot to load.
     */
    internal fun attemptToUpdateDeclaredShotState(name: String) {
        scope.launch {
            currentDeclaredShot = declaredShotRepository.fetchDeclaredShotFromName(name = name)
            currentDeclaredShot?.let { declaredShot ->
                createEditDeclaredShotMutableStateFlow.update {
                    it.copy(
                        currentDeclaredShot = declaredShot,
                        declaredShotState = DeclaredShotState.VIEWING
                    )
                }
            }
        }
    }

    /**
     * Handles confirmation to delete a declared shot.
     * Checks to see if the shot is being used by a player.
     * If it is, it will not be deleted.
     *
     * @param shotName Name of the shot.
     * @param shotKey Firebase key if the shot exists in Firebase.
     * @param id Local database ID of the shot.
     */
    suspend fun onYesDeleteShot(shotName: String, shotKey: String, id: Int) {
        navigation.enableProgress(progress = Progress())

        val loggedShotNames = playerRepository.fetchAllPlayers().flatMap { player -> player.shotsLoggedList.map { shotLogged -> shotLogged.shotName } }

        if (loggedShotNames.contains(shotName)) {
            navigation.disableProgress()
            navigation.alert(alert = buildCouldNotDeleteShotDueToItBeingUsedAlert(shotName = shotName))
        } else {
            val updatedIgnoredShotIds = shotIgnoringRepository
                .fetchAllIgnoringShots()
                .map { it.shotId } + id

            if (shotKey.isEmpty()) {
                handleIdOnlyDelete(shotName = shotName, id = id, ignoredIds = updatedIgnoredShotIds)
            } else {
                handleFullShotDelete(
                    shotName = shotName,
                    shotKey = shotKey,
                    id = id,
                    ignoredIds = updatedIgnoredShotIds
                )
            }
        }
    }

    /**
     * Deletes a declared shot locally and updates ignored IDs when it has no Firebase key.
     */
    private suspend fun handleIdOnlyDelete(shotName: String, id: Int, ignoredIds: List<Int>) {
        createFirebaseUserInfo
            .attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(ignoredIds)
            .collectLatest { result ->
                processDeleteResult(result.first, true, shotName, id)
            }
    }

    /**
     * Deletes a declared shot locally and in Firebase if it has a Firebase key.
     */
    private suspend fun handleFullShotDelete(shotName: String, shotKey: String, id: Int, ignoredIds: List<Int>) {
        combine(
            createFirebaseUserInfo.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(ignoredIds),
            deleteFirebaseUserInfo.deleteDeclaredShot(shotKey)
        ) { ignoreResult, deleteResult ->
            processDeleteResult(ignoreResult.first, deleteResult, shotName, id)
        }.collectLatest {}
    }

    /**
     * Processes the result of a declared shot delete operation.
     */
    private suspend fun processDeleteResult(idsToIgnoreSuccess: Boolean, deletedShotSuccess: Boolean, shotName: String, id: Int) {
        navigation.disableProgress()
        if (idsToIgnoreSuccess && deletedShotSuccess) {
            declaredShotRepository.deleteShotById(id)
            shotIgnoringRepository.createShotIgnoring(shotId = id)
            navigation.navigateToDeclaredShotList()
        } else {
            navigation.alert(alert = buildCouldNotDeleteShotAlert(shotName))
        }
    }

    // ----------- Alert builders -----------

    /** Builds alert when a declared shot could not be deleted due to it being used. */
    internal fun buildCouldNotDeleteShotDueToItBeingUsedAlert(shotName: String): Alert {
        return Alert(
            title = "Cannot Delete $shotName",
            description = "We cannot delete this shot because it is being used by a player. Please remove it from the player before deleting it.",
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert when a declared shot could not be deleted. */
    internal fun buildCouldNotDeleteShotAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToDeleteShot),
            description = application.getString(StringsIds.weCouldNotDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert asking the user to confirm deleting a declared shot. */
    internal fun buildDeleteShotAlert(shotName: String, shotKey: String, id: Int): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteShot),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeleteShot(shotName, shotKey, id) } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            )
        )
    }

    internal fun buildShotContentNotChangedAlert(): Alert {
        return Alert(
            title = "Shot Content Not Changed",
            description = "The shot content has not been changed. Please change content and try again.",
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert shown after a shot has been created. */
    internal fun buildShotHasBeenCreatedAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotXHasBeenCreated, shotName),
            description = application.getString(StringsIds.shotXHasBeenCreatedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert shown after a shot has been edited. */
    internal fun buildShotHasBeenEditedAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotXHasBeenEdited, shotName),
            description = application.getString(StringsIds.shotXHasBeenEditedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert when a shot could not be saved. */
    internal fun buildShotErrorAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotXHadIssueSavingDetails, shotName),
            description = application.getString(StringsIds.shotXHadIssueSavingDetailsDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert when shot name is missing. */
    internal fun buildShotNameNotAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotNameMissing),
            description = application.getString(StringsIds.shotNameMissingDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert when shot name already exists. */
    internal fun buildShotNameAlreadyExistAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotWithThatNameAlreadyExists),
            description = application.getString(StringsIds.shotWithThatNameAlreadyExistsDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert when category is missing. */
    internal fun buildShotCategoryNotAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotCategoryMissing),
            description = application.getString(StringsIds.shotCategoryMissingDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert when description is missing. */
    internal fun buildShotDescriptionNotAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotDescriptionMissing),
            description = application.getString(StringsIds.shotDescriptionMissingDesc),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /** Builds alert for either creation or editing success. */
    internal fun buildSubmitShotAlert(hasShotBeenCreated: Boolean, shotName: String): Alert {
        return if (hasShotBeenCreated) {
            buildShotHasBeenCreatedAlert(shotName)
        } else {
            buildShotHasBeenEditedAlert(shotName)
        }
    }

    // ----------- UI events and field updates -----------

    /** Shows delete confirmation dialog for the current declared shot. */
    fun onDeleteShotClicked(id: Int) {
        navigation.alert(
            alert = buildDeleteShotAlert(
                shotName = currentDeclaredShot?.title ?: "",
                shotKey = currentDeclaredShot?.firebaseKey ?: "",
                id = id
            )
        )
    }

    /** Switches the screen into editing mode. */
    fun onEditShotPencilClicked() {
        editedDeclaredShot = currentDeclaredShot
        createEditDeclaredShotMutableStateFlow.update {
            it.copy(declaredShotState = DeclaredShotState.EDITING)
        }
    }

    /** Updates the edited declared shot's name. */
    fun onEditShotNameValueChanged(shotName: String) {
        editedDeclaredShot = editedDeclaredShot?.copy(title = shotName)
    }

    /** Updates the edited declared shot's category. */
    fun onEditShotCategoryValueChanged(shotCategory: String) {
        editedDeclaredShot = editedDeclaredShot?.copy(shotCategory = shotCategory)
    }

    /** Updates the edited declared shot's description. */
    fun onEditShotDescriptionValueChanged(description: String) {
        editedDeclaredShot = editedDeclaredShot?.copy(description = description)
    }

    /** Updates the new declared shot's name field during creation. */
    fun onCreateShotNameValueChanged(shotName: String) {
        createdShotInfo = createdShotInfo.copy(name = shotName)
    }

    /** Updates the new declared shot's description field during creation. */
    fun onCreateShotDescriptionValueChanged(shotDescription: String) {
        createdShotInfo = createdShotInfo.copy(description = shotDescription)
    }

    /** Updates the new declared shot's category field during creation. */
    fun onCreateShotCategoryValueChanged(shotCategory: String) {
        createdShotInfo = createdShotInfo.copy(category = shotCategory)
    }

    /**
     * Submits either an edited or newly created declared shot.
     * Validates input and synchronizes data with Firebase and local storage.
     */
    fun onEditOrCreateNewShot() {
        scope.launch {
            val shotState = createEditDeclaredShotMutableStateFlow.value.declaredShotState
            navigation.enableProgress(Progress())

            when (shotState) {
                DeclaredShotState.EDITING -> handleShotEdit()
                else -> handleShotCreation()
            }
        }
    }

    /**
     * Displays a generic error alert when a shot fails to save.
     */
    private fun handleShotError(shotName: String) {
        navigation.disableProgress()
        navigation.alert(buildShotErrorAlert(shotName))
    }

    /**
     * Handles validation and submission for editing a declared shot.
     */
    private suspend fun handleShotEdit() {
        val shot = editedDeclaredShot ?: run {
            handleShotError(application.getString(StringsIds.empty))
            return
        }

        val isExactShotNameMatchIgnoringSpaces = shot.title.isNotEmpty() &&
            allDeclaredShotNames.any { it.normalizeSpaces() == shot.title.normalizeSpaces() }

        val missingFieldAlert = when {
            shot.title.isEmpty() -> buildShotNameNotAddedAlert()
            shot.shotCategory.isEmpty() -> buildShotCategoryNotAddedAlert()
            shot.description.isEmpty() -> buildShotDescriptionNotAddedAlert()
            isExactShotNameMatchIgnoringSpaces -> buildShotContentNotChangedAlert()
            else -> null
        }

        if (missingFieldAlert != null) {
            navigation.disableProgress()
            navigation.alert(missingFieldAlert)
            return
        }

        shot.firebaseKey?.let { declaredShotFirebaseKey ->
            submitUpdatedShotToRepositoryAndFirebase(
                declaredShotWithKeyRealtimeResponse = DeclaredShotWithKeyRealtimeResponse(
                    declaredShotFirebaseKey = declaredShotFirebaseKey,
                    declaredShotRealtimeResponse = DeclaredShotRealtimeResponse(
                        id = shot.id,
                        shotCategory = shot.shotCategory,
                        title = shot.title,
                        description = shot.description
                    )
                )
            )
        } ?: run {
            submitShotToRepositoryAndFirebase(shot = shot, shouldDeleteShotById = true, hasShotBeenCreated = false)
        }
    }

    /**
     * Handles validation and submission for creating a declared shot.
     */
    private suspend fun handleShotCreation() {
        val isExactShotNameMatchIgnoringSpaces = createdShotInfo.name.isNotEmpty() &&
            allDeclaredShotNames.any { it.normalizeSpaces() == createdShotInfo.name.normalizeSpaces() }

        val missingFieldAlert = when {
            createdShotInfo.name.isEmpty() -> buildShotNameNotAddedAlert()
            createdShotInfo.category.isEmpty() -> buildShotCategoryNotAddedAlert()
            createdShotInfo.description.isEmpty() -> buildShotDescriptionNotAddedAlert()
            isExactShotNameMatchIgnoringSpaces -> buildShotNameAlreadyExistAlert()
            else -> null
        }

        if (missingFieldAlert != null) {
            navigation.disableProgress()
            navigation.alert(missingFieldAlert)
            return
        }

        val declaredShot = DeclaredShot(
            id = declaredShotRepository.fetchMaxId() + 1,
            shotCategory = createdShotInfo.category,
            title = createdShotInfo.name,
            description = createdShotInfo.description,
            firebaseKey = null
        )

        submitShotToRepositoryAndFirebase(shot = declaredShot, shouldDeleteShotById = false, hasShotBeenCreated = true)
    }

    /**
     * Submits an updated declared shot to Firebase and updates it in local repository.
     */
    private suspend fun submitUpdatedShotToRepositoryAndFirebase(declaredShotWithKeyRealtimeResponse: DeclaredShotWithKeyRealtimeResponse) {
        updateFirebaseUserInfo.updateDeclaredShot(declaredShotWithKeyRealtimeResponse)
            .collectLatest { success ->
                val newDeclaredShot = DeclaredShot(
                    id = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                    shotCategory = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                    title = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                    description = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description,
                    firebaseKey = declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey
                )

                if (success) {
                    declaredShotRepository.updateDeclaredShot(newDeclaredShot)
                    navigation.disableProgress()
                    navigation.navigateToDeclaredShotList()
                    navigation.alert(buildSubmitShotAlert(hasShotBeenCreated = false, shotName = newDeclaredShot.title))
                } else {
                    handleShotError(newDeclaredShot.title)
                }
            }
    }

    /**
     * Submits a declared shot to Firebase and saves it in local repository.
     * Used for shot creation or when editing and re-creating locally.
     */
    private suspend fun submitShotToRepositoryAndFirebase(
        shot: DeclaredShot,
        shouldDeleteShotById: Boolean,
        hasShotBeenCreated: Boolean
    ) {
        createFirebaseUserInfo.attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(shot)
            .collectLatest { (success, firebaseKey) ->
                if (success) {
                    if (shouldDeleteShotById) {
                        declaredShotRepository.deleteShotById(shot.id)
                    }
                    declaredShotRepository.createNewDeclaredShot(shot.copy(firebaseKey = firebaseKey))
                    navigation.disableProgress()
                    navigation.navigateToDeclaredShotList()
                    navigation.alert(buildSubmitShotAlert(hasShotBeenCreated = hasShotBeenCreated, shotName = shot.title))
                } else {
                    handleShotError(shot.title)
                }
            }
    }
}

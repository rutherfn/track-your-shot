package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
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
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditDeclaredShotViewModel(
    private val application: Application,
    private val declaredShotRepository: DeclaredShotRepository,
    private val shotIgnoringRepository: ShotIgnoringRepository,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val updateFirebaseUserInfo: UpdateFirebaseUserInfo,
    private val deleteFirebaseUserInfo: DeleteFirebaseUserInfo,
    private val createSharedPreferences: CreateSharedPreferences,
    private val readSharedPreferences: ReadSharedPreferences,
    private val navigation: CreateEditDeclaredShotNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal var allDeclaredShotNames: List<String> = emptyList()
    internal var currentDeclaredShot: DeclaredShot? = null
    internal var editedDeclaredShot: DeclaredShot? = null

    internal var createdShotInfo = CreateShotInfo()

    internal var createEditDeclaredShotMutableStateFlow = MutableStateFlow(value = CreateEditDeclaredShotState())
    var createEditDeclaredShotStateFlow = createEditDeclaredShotMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()

        scope.launch {
            allDeclaredShotNames = declaredShotRepository.fetchAllDeclaredShots().map { it.title }
        }
        val name = readSharedPreferences.declaredShotName()

        if (name != "") {
            attemptToUpdateDeclaredShotState(name = name)
        } else {
            currentDeclaredShot = null
            createEditDeclaredShotMutableStateFlow.update { state ->
                state.copy(
                    declaredShotState = DeclaredShotState.CREATING,
                    currentDeclaredShot = null
                )
            }
        }
    }

    fun onToolbarMenuClicked() {
        val declaredShotState = createEditDeclaredShotMutableStateFlow.value.declaredShotState

        if (declaredShotState == DeclaredShotState.EDITING) {
            createEditDeclaredShotMutableStateFlow.update { state ->
                state.copy(declaredShotState = DeclaredShotState.VIEWING)
            }
            resetDeclaredShotValues()
        } else {
            resetDeclaredShotValues()
            navigation.pop()
        }
    }

    private fun resetDeclaredShotValues() {
        currentDeclaredShot = null
        editedDeclaredShot = null
    }

    internal fun attemptToUpdateDeclaredShotState(name: String) {
        scope.launch {
            currentDeclaredShot = declaredShotRepository.fetchDeclaredShotFromName(name = name)

            createSharedPreferences.createDeclaredShotName(value = "")
            currentDeclaredShot?.let { declaredShot ->
                createEditDeclaredShotMutableStateFlow.update { state ->
                    state.copy(
                        currentDeclaredShot = declaredShot,
                        declaredShotState = DeclaredShotState.VIEWING
                    )
                }
            }
        }
    }

    suspend fun onYesDeleteShot(shotName: String, shotKey: String, id: Int) {
        navigation.enableProgress(progress = Progress())

        val updatedIgnoredShotIds = shotIgnoringRepository
            .fetchAllIgnoringShots()
            .map { it.shotId } + id

        if (shotKey.isEmpty()) {
            handleIdOnlyDelete(
                shotName = shotName,
                id = id,
                ignoredIds = updatedIgnoredShotIds
            )
        } else {
            handleFullShotDelete(
                shotName = shotName,
                shotKey = shotKey,
                id = id,
                ignoredIds = updatedIgnoredShotIds
            )
        }
    }

    private suspend fun handleIdOnlyDelete(shotName: String, id: Int, ignoredIds: List<Int>) {
        createFirebaseUserInfo
            .attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(ignoredIds)
            .collectLatest { result ->
                processDeleteResult(
                    idsToIgnoreSuccess = result.first,
                    deletedShotSuccess = true,
                    shotName = shotName,
                    id = id
                )
            }
    }

    private suspend fun handleFullShotDelete(shotName: String, shotKey: String, id: Int, ignoredIds: List<Int>) {
        combine(
            createFirebaseUserInfo.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(ignoredIds),
            deleteFirebaseUserInfo.deleteDeclaredShot(shotKey)
        ) { ignoreResult, deleteResult ->
            processDeleteResult(
                idsToIgnoreSuccess = ignoreResult.first,
                deletedShotSuccess = deleteResult,
                shotName = shotName,
                id = id
            )
        }.collectLatest { }
    }

    private suspend fun processDeleteResult(idsToIgnoreSuccess: Boolean, deletedShotSuccess: Boolean, shotName: String, id: Int) {
        navigation.disableProgress()

        if (idsToIgnoreSuccess && deletedShotSuccess) {
            declaredShotRepository.deleteShotById(id)
            shotIgnoringRepository.createShotIgnoring(shotId = id)
            navigation.pop()
        } else {
            navigation.alert(alert = buildCouldNotDeleteShotAlert(shotName))
        }
    }

    internal fun buildCouldNotDeleteShotAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToDeleteShot),
            description = application.getString(StringsIds.weCouldNotDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt),
                onButtonClicked = { }
            )
        )
    }

    internal fun buildDeleteShotAlert(shotName: String, shotKey: String, id: Int): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteShot),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeleteShot(shotName = shotName, shotKey = shotKey, id = id) } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            )
        )
    }

    internal fun buildShotHasBeenCreatedAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotXHasBeenCreated, shotName),
            description = application.getString(StringsIds.shotXHasBeenCreatedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildShotHasBeenEditedAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotXHasBeenEdited, shotName),
            description = application.getString(StringsIds.shotXHasBeenEditedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildShotErrorAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotXHadIssueSavingDetails, shotName),
            description = application.getString(StringsIds.shotXHadIssueSavingDetailsDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildShotNameNotAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotNameMissing),
            description = application.getString(StringsIds.shotNameMissingDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildShotNameAlreadyExistAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotWithThatNameAlreadyExists),
            description = application.getString(StringsIds.shotWithThatNameAlreadyExistsDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildShotCategoryNotAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotCategoryMissing),
            description = application.getString(StringsIds.shotCategoryMissingDescription),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildShotDescriptionNotAddedAlert(): Alert {
        return Alert(
            title = application.getString(StringsIds.shotDescriptionMissing),
            description = application.getString(StringsIds.shotDescriptionMissingDesc),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    internal fun buildSubmitShotAlert(hasShotBeenCreated: Boolean, shotName: String): Alert {
        return if (hasShotBeenCreated) {
            buildShotHasBeenCreatedAlert(shotName = shotName)
        } else {
            buildShotHasBeenEditedAlert(shotName = shotName)
        }
    }

    fun onDeleteShotClicked(id: Int) = navigation.alert(alert = buildDeleteShotAlert(shotName = currentDeclaredShot?.title ?: "", shotKey = currentDeclaredShot?.firebaseKey ?: "", id = id))

    fun onEditShotPencilClicked() {
        editedDeclaredShot = currentDeclaredShot
        createEditDeclaredShotMutableStateFlow.update { state ->
            state.copy(
                declaredShotState = DeclaredShotState.EDITING
            )
        }
    }

    fun onEditShotNameValueChanged(shotName: String) {
        editedDeclaredShot?.let { declaredShot ->
            editedDeclaredShot = declaredShot.copy(title = shotName)
        }
    }

    fun onEditShotCategoryValueChanged(shotCategory: String) {
        editedDeclaredShot?.let { declaredShot ->
            editedDeclaredShot = declaredShot.copy(shotCategory = shotCategory)
        }
    }

    fun onEditShotDescriptionValueChanged(description: String) {
        editedDeclaredShot?.let { declaredShot ->
            editedDeclaredShot = declaredShot.copy(description = description)
        }
    }

    fun onCreateShotNameValueChanged(shotName: String) {
        createdShotInfo = createdShotInfo.copy(name = shotName)
    }

    fun onCreateShotDescriptionValueChanged(shotDescription: String) {
        createdShotInfo = createdShotInfo.copy(description = shotDescription)
    }

    fun onCreateShotCategoryValueChanged(shotCategory: String) {
        createdShotInfo = createdShotInfo.copy(category = shotCategory)
    }

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

    private fun handleShotError(shotName: String) {
        navigation.disableProgress()
        navigation.alert(buildShotErrorAlert(shotName = shotName))
    }

    private suspend fun handleShotEdit() {
        val shot = editedDeclaredShot ?: run {
            handleShotError(shotName = application.getString(StringsIds.empty))
            return
        }

        val isExactShotNameMatchIgnoringSpaces =
            shot.title.isNotEmpty() && allDeclaredShotNames.any { it.normalizeSpaces() == shot.title.normalizeSpaces() }

        val missingFieldAlert = when {
            shot.title.isEmpty() -> buildShotNameNotAddedAlert()
            shot.shotCategory.isEmpty() -> buildShotCategoryNotAddedAlert()
            shot.description.isEmpty() -> buildShotDescriptionNotAddedAlert()
            isExactShotNameMatchIgnoringSpaces -> buildShotDescriptionNotAddedAlert()
            else -> null
        }

        if (missingFieldAlert != null) {
            navigation.disableProgress()
            navigation.alert(alert = missingFieldAlert)
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

    private suspend fun handleShotCreation() {
        val isExactShotNameMatchIgnoringSpaces =
            createdShotInfo.name.isNotEmpty() && allDeclaredShotNames.any { it.normalizeSpaces() == createdShotInfo.name.normalizeSpaces() }
        val missingFieldAlert = when {
            createdShotInfo.name.isEmpty() -> buildShotNameNotAddedAlert()
            createdShotInfo.category.isEmpty() -> buildShotCategoryNotAddedAlert()
            createdShotInfo.description.isEmpty() -> buildShotDescriptionNotAddedAlert()
            isExactShotNameMatchIgnoringSpaces -> buildShotNameAlreadyExistAlert()
            else -> null
        }

        if (missingFieldAlert != null) {
            navigation.disableProgress()
            navigation.alert(alert = missingFieldAlert)
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

    private suspend fun submitUpdatedShotToRepositoryAndFirebase(declaredShotWithKeyRealtimeResponse: DeclaredShotWithKeyRealtimeResponse) {
        updateFirebaseUserInfo
            .updateDeclaredShot(declaredShotWithKeyRealtimeResponse = declaredShotWithKeyRealtimeResponse)
            .collectLatest { success ->

                val newDeclaredShot = DeclaredShot(
                    id = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.id,
                    shotCategory = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.shotCategory,
                    title = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.title,
                    description = declaredShotWithKeyRealtimeResponse.declaredShotRealtimeResponse.description,
                    firebaseKey = declaredShotWithKeyRealtimeResponse.declaredShotFirebaseKey
                )

                if (success) {
                    declaredShotRepository.updateDeclaredShot(declaredShot = newDeclaredShot)
                    navigation.disableProgress()
                    navigation.pop()
                    navigation.alert(buildSubmitShotAlert(hasShotBeenCreated = false, shotName = newDeclaredShot.title))
                } else {
                    handleShotError(shotName = newDeclaredShot.title)
                }
            }
    }

    private suspend fun submitShotToRepositoryAndFirebase(shot: DeclaredShot, shouldDeleteShotById: Boolean, hasShotBeenCreated: Boolean) {
        createFirebaseUserInfo
            .attemptToCreateDeclaredShotFirebaseRealtimeDatabaseResponseFlow(shot)
            .collectLatest { (success, firebaseKey) ->

                if (success) {
                    if (shouldDeleteShotById) {
                        declaredShotRepository.deleteShotById(shot.id)
                    }
                    declaredShotRepository.createNewDeclaredShot(shot.copy(firebaseKey = firebaseKey))
                    navigation.disableProgress()
                    navigation.pop()
                    navigation.alert(buildSubmitShotAlert(hasShotBeenCreated = hasShotBeenCreated, shotName = shot.title))
                } else {
                    handleShotError(shotName = shot.title)
                }
            }
    }
}

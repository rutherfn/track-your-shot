package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.repository.ShotIgnoringRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotIgnoring
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.firebase.core.create.CreateFirebaseUserInfo
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditDeclaredShotViewModel(
    private val application: Application,
    private val declaredShotRepository: DeclaredShotRepository,
    private val shotIgnoringRepository: ShotIgnoringRepository,
    private val createFirebaseUserInfo: CreateFirebaseUserInfo,
    private val createSharedPreferences: CreateSharedPreferences,
    private val readSharedPreferences: ReadSharedPreferences,
    private val navigation: CreateEditDeclaredShotNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal var currentDeclaredShot: DeclaredShot? = null

    internal var createEditDeclaredShotMutableStateFlow = MutableStateFlow(value = CreateEditDeclaredShotState())
    var createEditDeclaredShotStateFlow = createEditDeclaredShotMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        val id = readSharedPreferences.declaredShotId()

        if (id != 0) {
            attemptToUpdateDeclaredShotState(id = id)
        } else {
            createEditDeclaredShotMutableStateFlow.update { state ->
                state.copy(
                    declaredShotState = DeclaredShotState.CREATING,
                    toolbarTitle = application.getString(StringsIds.createShot)
                )
            }
        }
    }

    fun onToolbarMenuClicked() = navigation.pop()

    private fun attemptToUpdateDeclaredShotState(id: Int) {
        scope.launch {
            currentDeclaredShot = declaredShotRepository.fetchDeclaredShotFromId(id = id)

            createSharedPreferences.createDeclaredShotId(value = 0)
            currentDeclaredShot?.let { declaredShot ->
                createEditDeclaredShotMutableStateFlow.update { state ->
                    state.copy(
                        currentDeclaredShot = declaredShot,
                        declaredShotState = DeclaredShotState.VIEWING,
                        toolbarTitle = application.getString(
                            StringsIds.viewX,
                            declaredShot.title
                        )
                    )
                }
            }
        }
    }

    suspend fun onYesDeleteShot(shotName: String, id: Int) {
        navigation.enableProgress(progress = Progress())
        val currentIdsToIgnore = shotIgnoringRepository.fetchAllIgnoringShots().map { it.shotId }

        createFirebaseUserInfo.attemptToCreateDefaultShotIdsToIgnoreFirebaseRealTimeDatabaseResponseFlow(defaultShotIdsToIgnore = currentIdsToIgnore).collectLatest { result ->
            if (result.first) {
                declaredShotRepository.deleteShotById(id = id)
                shotIgnoringRepository.createShotIgnoring(shotId = id)
                navigation.pop()
                navigation.disableProgress()
            } else {
                navigation.disableProgress()
                navigation.alert(alert = buildCouldNotDeleteShotAlert(shotName = shotName))
            }
        }
    }

    fun buildCouldNotDeleteShotAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.unableToDeleteShot),
            description = application.getString(StringsIds.weCouldNotDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt),
                onButtonClicked = {  }
            )
        )
    }

    fun buildDeleteShotAlert(shotName: String, id: Int): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteShot),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeleteShot(shotName = shotName, id = id) } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            )
        )
    }

    fun onDeleteShotClicked(id: Int) {
        navigation.alert(alert = buildDeleteShotAlert(shotName = currentDeclaredShot?.title ?: "", id = id))
    }
}

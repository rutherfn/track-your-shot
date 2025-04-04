package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEditDeclaredShotViewModel(
    private val declaredShotRepository: DeclaredShotRepository,
    private val readSharedPreferences: ReadSharedPreferences,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal var currentDeclaredShot: DeclaredShot? = null

    internal var createEditDeclaredShotMutableStateFlow = MutableStateFlow(value = CreateEditDeclaredShotState())
    var createEditDeclaredShotStateFlow = createEditDeclaredShotMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()

    }

    fun attemptToUpdateDeclaredShotState(id: Int) {
        scope.launch {
            currentDeclaredShot = declaredShotRepository.fetchDeclaredShotFromId(id = id)

            createEditDeclaredShotMutableStateFlow.update { state ->
                state.copy(currentDeclaredShot = currentDeclaredShot)
            }
        }
    }
}
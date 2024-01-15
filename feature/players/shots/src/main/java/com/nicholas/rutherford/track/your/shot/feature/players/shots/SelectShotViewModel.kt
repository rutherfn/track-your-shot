package com.nicholas.rutherford.track.your.shot.feature.players.shots

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectShotViewModel(
    private val scope: CoroutineScope,
    private val declaredShotRepository: DeclaredShotRepository
) : ViewModel() {

    internal var currentDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

    internal val selectShotMutableStateFlow = MutableStateFlow(value = SelectShotState())
    val selectShotStateFlow = selectShotMutableStateFlow.asStateFlow()

    init {
        updateDeclaredShotListState()
    }

    fun updateDeclaredShotListState() {
        scope.launch {
            declaredShotRepository.fetchAllDeclaredShots().forEach { declaredShot ->
                currentDeclaredShotArrayList.add(declaredShot)
            }

            selectShotMutableStateFlow.update { state ->
                state.copy(declaredShotList = currentDeclaredShotArrayList)
            }
        }
    }

    fun onHelpIconClicked() {
        // todo show a alert of some sort with info
    }

    fun onDeclaredShotItemClicked() {
        // todo -> navigate user to new screen
    }
}

package com.nicholas.rutherford.track.your.shot.feature.players.shots

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.helper.account.AccountAuthManager
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import com.nicholas.rutherford.track.your.shot.shared.preference.read.ReadSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectShotViewModel(
    private val scope: CoroutineScope,
    private val navigation: SelectShotNavigation,
    private val declaredShotRepository: DeclaredShotRepository,
    private val accountAuthManager: AccountAuthManager,
    private val createSharedPreferences: CreateSharedPreferences,
    private val readSharedPreferences: ReadSharedPreferences
) : ViewModel() {

    internal var currentDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

    internal val selectShotMutableStateFlow = MutableStateFlow(value = SelectShotState())
    val selectShotStateFlow = selectShotMutableStateFlow.asStateFlow()

    init {
        fetchDeclaredShotsAndUpdateState()
        collectLoggedInDeclaredShotsStateFlow()
    }

    internal fun fetchDeclaredShotsAndUpdateState() {
        scope.launch {
            currentDeclaredShotArrayList.addAll(declaredShotRepository.fetchAllDeclaredShots())
            selectShotMutableStateFlow.update { state ->
                state.copy(searchQuery = "", declaredShotList = currentDeclaredShotArrayList)
            }
        }
    }

    private fun collectLoggedInDeclaredShotsStateFlow() {
        scope.launch {
            accountAuthManager.loggedInDeclaredShotListStateFlow.collectLatest { declaredShotList ->
                if (shouldUpdateStateFromLoggedIn(
                        declaredShotList = declaredShotList,
                        shouldUpdateLoggedInDeclaredShotListState = readSharedPreferences.shouldUpdateLoggedInDeclaredShotListState()
                    )
                ) {
                    currentDeclaredShotArrayList.addAll(declaredShotList)
                    selectShotMutableStateFlow.update { state ->
                        state.copy(searchQuery = "", declaredShotList = currentDeclaredShotArrayList)
                    }
                    createSharedPreferences.createShouldUpdateLoggedInDeclaredShotListPreference(value = false)
                }
            }
        }
    }

    internal fun shouldUpdateStateFromLoggedIn(declaredShotList: List<DeclaredShot>, shouldUpdateLoggedInDeclaredShotListState: Boolean): Boolean {
        return declaredShotList.isNotEmpty() && shouldUpdateLoggedInDeclaredShotListState
    }

    fun onSearchValueChanged(newSearchQuery: String) {
        currentDeclaredShotArrayList.clear()

        scope.launch {
            declaredShotRepository.fetchDeclaredShotsBySearchQuery(searchQuery = newSearchQuery).forEach { declaredShot ->
                currentDeclaredShotArrayList.add(declaredShot)
            }
            selectShotMutableStateFlow.update { state ->
                state.copy(declaredShotList = currentDeclaredShotArrayList, searchQuery = newSearchQuery)
            }
        }
    }

    fun onCancelIconClicked() {
        if (selectShotMutableStateFlow.value.searchQuery.isNotEmpty()) {
            currentDeclaredShotArrayList.clear()
            fetchDeclaredShotsAndUpdateState()
        }
    }

    fun onBackButtonClicked(fromCreatePlayer: Boolean = true) {
        if (fromCreatePlayer) {
            navigation.popFromCreatePlayer()
        } else {
            navigation.popFromEditPlayer()
        }
    }

    fun onHelpIconClicked() {
        // todo show a alert of some sort with info
    }

    fun onDeclaredShotItemClicked() {
        // todo -> navigate user to new screen
    }
}

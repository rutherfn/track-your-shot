package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Declared Shots List screen, responsible for retrieving and presenting
 * a list of declared shots. It manages interactions such as clicking on existing declared shots,
 * adding new ones, and toolbar menu actions.
 *
 * @property declaredShotRepository Repository for accessing declared shot data from local storage.
 * @property createSharedPreferences Interface for writing shot-related values to shared preferences.
 * @property navigation Interface for managing navigation actions from the declared shots list screen.
 * @property scope CoroutineScope used for launching asynchronous operations.
 */
class DeclaredShotsListViewModel(
    private val declaredShotRepository: DeclaredShotRepository,
    private val createSharedPreferences: CreateSharedPreferences,
    private val navigation: DeclaredShotsListNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    /** Holds the currently fetched list of declared shots. */
    internal var currentDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

    internal var declaredShotsListMutableStateFlow = MutableStateFlow(value = DeclaredShotsListState())

    /** State flow representing the current UI state of the declared shots list screen. */
    val declaredShotsListStateFlow = declaredShotsListMutableStateFlow.asStateFlow()

    init {
        initializeDeclaredShotsScreen()
    }

    /**
     * Initializes the Declared Shots List screen by:
     * - Updating the UI state with the current list of declared shots.
     *
     * This function is separated for testability.
     */
    internal fun initializeDeclaredShotsScreen() {
        updateDeclaredShotsListState()
    }

    /**
     * Fetches all declared shots from the repository and updates the UI state accordingly.
     */
    fun updateDeclaredShotsListState() {
        scope.launch {
            currentDeclaredShotArrayList.clear()
            declaredShotRepository.fetchAllDeclaredShots().forEach { declaredShot ->
                currentDeclaredShotArrayList.add(declaredShot)
            }
            declaredShotsListMutableStateFlow.update { state ->
                state.copy(declaredShotsList = currentDeclaredShotArrayList.toList())
            }
        }
    }

    /**
     * Handles the toolbar menu click event by navigating back.
     */
    fun onToolbarMenuClicked() = navigation.pop()

    /**
     * Handles the event when a declared shot item is clicked.
     * Stores the selected shot title and then will navigate to the edit screen.
     *
     * @param title The title of the selected declared shot.
     */
    fun onDeclaredShotClicked(title: String) {
        scope.launch {
            navigation.enableProgress(Progress())
            navigation.disableProgress()
            navigation.createEditDeclaredShot(shotName = title)
        }
    }

    /**
     * Handles the action of adding a new declared shot.
     * Navigates to the create screen.
     */
    fun onAddDeclaredShotClicked() {
        navigation.createEditDeclaredShot(shotName = "")
    }
}


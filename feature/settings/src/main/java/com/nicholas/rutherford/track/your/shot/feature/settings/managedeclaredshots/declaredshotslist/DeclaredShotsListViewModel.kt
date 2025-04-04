package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.room.repository.DeclaredShotRepository
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeclaredShotsListViewModel(
    private val declaredShotRepository: DeclaredShotRepository,
    private val createSharedPreferences: CreateSharedPreferences,
    private val navigation: DeclaredShotsListNavigation,
    private val scope: CoroutineScope
) : BaseViewModel() {

    internal var currentDeclaredShotArrayList: ArrayList<DeclaredShot> = arrayListOf()

    internal var declaredShotsListMutableStateFlow = MutableStateFlow(value = DeclaredShotsListState())
    val declaredShotsListStateFlow = declaredShotsListMutableStateFlow.asStateFlow()

    override fun onNavigatedTo() {
        super.onNavigatedTo()
        updateDeclaredShotsListState()
    }

    fun updateDeclaredShotsListState() {
        scope.launch {
            currentDeclaredShotArrayList.clear()
            declaredShotRepository.fetchAllDeclaredShots().forEach { declaredShot ->
                currentDeclaredShotArrayList.add(declaredShot)
            }
            declaredShotsListMutableStateFlow.update { state -> state.copy(declaredShotsList = currentDeclaredShotArrayList.toList()) }
        }
    }

    fun onToolbarMenuClicked() = navigation.pop()

    fun onDeclaredShotClicked(id: Int) {
        createSharedPreferences.createDeclaredShotId(value = id)
        navigation.createEditDeclaredShot()
    }

    fun onAddDeclaredShotClicked() = navigation.createEditDeclaredShot()
}

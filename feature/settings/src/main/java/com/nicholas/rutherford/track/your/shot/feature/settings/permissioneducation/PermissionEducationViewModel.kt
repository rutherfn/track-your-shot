package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Permission Education screen.
 *
 * This ViewModel manages the state and behavior of the permission education flow,
 * including the list of educational slides about required permissions, and navigation
 * actions when the user interacts with the UI.
 *
 * @property navigation Interface for navigating between screens and URLs.
 * @property application Android application context used for string resources.
 */
class PermissionEducationViewModel(
    private val navigation: PermissionEducationNavigation,
    private val application: Application
) : BaseViewModel() {

    /** Mutable state flow holding the current state of the permission education screen. */
    internal val permissionEducationMutableStateFlow = MutableStateFlow(value = PermissionEducationState())

    /** Public read-only state flow for observing UI updates. */
    val permissionEducationStateFlow = permissionEducationMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    /**
     * Handles the event when the "Got It" button is clicked.
     * Navigates back to the previous screen.
     */
    fun onGotItButtonClicked() = navigation.pop()

    /**
     * Handles the event when the "More Info" button is clicked.
     * Navigates to a URL providing additional details about Android permissions.
     */
    fun onMoreInfoClicked() = navigation.navigateToUrl(url = application.getString(StringsIds.androidPermissionsUrl))

    /**
     * Builds the list of education content for permissions.
     *
     * @return A list of [EducationInfo] items used to inform the user about permission usage.
     */
    private fun educationInfoList(): List<EducationInfo> {
        return listOf(
            EducationInfo(
                title = application.getString(StringsIds.cameraPermission),
                description = application.getString(StringsIds.cameraPermissionExplanation),
                drawableResId = DrawablesIds.camera,
                buttonText = application.getString(StringsIds.gotIt),
                moreInfoVisible = true
            )
        )
    }

    /**
     * Updates the state with the current list of permission education content.
     */
    private fun updateState() {
        permissionEducationMutableStateFlow.update { permissionEducationState ->
            permissionEducationState.copy(
                educationInfoList = educationInfoList()
            )
        }
    }
}


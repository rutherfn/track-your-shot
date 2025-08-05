package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the Onboarding Education screen.
 * Manages the state of educational content pages shown during onboarding and handles navigation events.
 *
 * @property savedStateHandle Provides access to saved state, including a flag indicating if this is the first time the screen is launched.
 * @property navigation Handles navigation actions triggered from the onboarding education screen.
 * @property application Provides access to application resources such as strings and drawables.
 */
class OnboardingEducationViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigation: OnboardingEducationNavigation,
    private val application: Application
) : BaseViewModel() {

    /** Indicates whether this onboarding screen is shown on first launch */
    internal val isFirstTimeLaunchedParam: Boolean = savedStateHandle.get<Boolean>("isFirstTimeLaunched") ?: false

    /** Backing mutable state flow holding the current UI state */
    internal val onboardingEducationMutableStateFlow = MutableStateFlow(value = OnboardingEducationState())

    /** Publicly exposed immutable state flow for UI to observe */
    val onboardingEducationStateFlow = onboardingEducationMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    /**
     * Builds the list of educational content pages shown during onboarding.
     *
     * @return List of [EducationInfo] representing the pages.
     */
    internal fun buildEducationInfoList(): List<EducationInfo> {
        return listOf(
            EducationInfo(
                title = application.getString(StringsIds.createYourPlayersProfile),
                description = application.getString(StringsIds.createYourPlayerProfileDescription),
                drawableResId = DrawablesIds.createPlayers,
                buttonText = application.getString(StringsIds.next)
            ),
            EducationInfo(
                title = application.getString(StringsIds.logYourShots),
                description = application.getString(StringsIds.logYourShotsDescription),
                drawableResId = DrawablesIds.statisticBasketball,
                buttonText = application.getString(StringsIds.next)
            ),
            EducationInfo(
                title = application.getString(StringsIds.trackYourProgress),
                description = application.getString(StringsIds.trackYourProgressDescription),
                drawableResId = DrawablesIds.basketballShotMade,
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    /**
     * Updates the current UI state with the built education info list.
     */
    fun updateState() {
        onboardingEducationMutableStateFlow.update {
            it.copy(educationInfoList = buildEducationInfoList())
        }
    }

    /**
     * Handles the event when the "Got It" button is clicked.
     * Triggers navigation back, passing whether this was the first time launch.
     */
    fun onGotItButtonClicked() = navigation.pop(isFirstTimeLaunchedParam = isFirstTimeLaunchedParam)
}

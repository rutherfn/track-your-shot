package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OnboardingEducationViewModel(
    private val navigation: OnboardingEducationNavigation,
    private val application: Application
) : ViewModel() {

    internal val onboardingEducationMutableStateFlow = MutableStateFlow(value = OnboardingEducationState())
    val onboardingEducationStateFlow = onboardingEducationMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    fun buildEducationInfoList(): List<EducationInfo> {
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

    fun updateState() {
        onboardingEducationMutableStateFlow.update {
            it.copy(educationInfoList = buildEducationInfoList())
        }
    }

    fun onGotItButtonClicked() = navigation.pop()
}
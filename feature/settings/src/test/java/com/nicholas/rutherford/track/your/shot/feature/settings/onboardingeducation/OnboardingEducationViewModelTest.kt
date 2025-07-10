package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OnboardingEducationViewModelTest {

    private lateinit var onboardingEducationViewModel: OnboardingEducationViewModel

    private val navigation = mockk<OnboardingEducationNavigation>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        every { application.getString(StringsIds.createYourPlayersProfile) } returns "Create Your Players Profile"
        every { application.getString(StringsIds.createYourPlayerProfileDescription) } returns "Begin by creating a player profile, including details like first name, last name, position, and a player image."
        every { application.getString(StringsIds.logYourShots) } returns "Log Your Shots"
        every { application.getString(StringsIds.logYourShotsDescription) } returns "Choose the type of shot you want to record and input the makes and misses for the day the player took them."
        every { application.getString(StringsIds.trackYourProgress) } returns "Track Your Progress"
        every { application.getString(StringsIds.trackYourProgressDescription) } returns "Record shooting percentages for various shot types over multiple days and view the saved stats directly in the mobile app."
        every { application.getString(StringsIds.gotIt) } returns "Got It"
        every { application.getString(StringsIds.next) } returns "Next"

//        onboardingEducationViewModel = OnboardingEducationViewModel(
//            navigation = navigation,
//            application = application
//        )
    }

    @Test
    fun `build education info list should create education list for onboarding`() {
        val educationInfoList = listOf(
            EducationInfo(
                title = "Create Your Players Profile",
                description = "Begin by creating a player profile, including details like first name, last name, position, and a player image.",
                drawableResId = DrawablesIds.createPlayers,
                buttonText = "Next"
            ),
            EducationInfo(
                title = "Log Your Shots",
                description = "Choose the type of shot you want to record and input the makes and misses for the day the player took them.",
                drawableResId = DrawablesIds.statisticBasketball,
                buttonText = "Next"
            ),
            EducationInfo(
                title = "Track Your Progress",
                description = "Record shooting percentages for various shot types over multiple days and view the saved stats directly in the mobile app.",
                drawableResId = DrawablesIds.basketballShotMade,
                buttonText = "Got It"
            )
        )

        val result = onboardingEducationViewModel.buildEducationInfoList()

        Assertions.assertEquals(educationInfoList, result)
    }

    @Test
    fun `updateState should update state flow of a state`() {
        val educationInfoList = listOf(
            EducationInfo(
                title = "Create Your Players Profile",
                description = "Begin by creating a player profile, including details like first name, last name, position, and a player image.",
                drawableResId = DrawablesIds.createPlayers,
                buttonText = "Next"
            ),
            EducationInfo(
                title = "Log Your Shots",
                description = "Choose the type of shot you want to record and input the makes and misses for the day the player took them.",
                drawableResId = DrawablesIds.statisticBasketball,
                buttonText = "Next"
            ),
            EducationInfo(
                title = "Track Your Progress",
                description = "Record shooting percentages for various shot types over multiple days and view the saved stats directly in the mobile app.",
                drawableResId = DrawablesIds.basketballShotMade,
                buttonText = "Got It"
            )
        )

        onboardingEducationViewModel.updateState()

        val value = onboardingEducationViewModel.onboardingEducationMutableStateFlow.value

        Assertions.assertEquals(value.educationInfoList, educationInfoList)
    }

    @Test
    fun `on got it button clicked should pop stack`() {
        onboardingEducationViewModel.onGotItButtonClicked()

        //verify { navigation.pop() }
    }
}

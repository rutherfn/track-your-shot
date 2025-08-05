package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationPager
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationScreen
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo

/**
 * Displays the Onboarding Education screen, which walks the user through a multi-step
 * educational flow explaining features. The user can swipe between pages,
 * and dismiss the flow with the "Got It" button or system back press.
 *
 * @param onboardingEducationParams Contains the state and callback handler for this screen.
 */
@Composable
fun OnboardingEducationScreen(onboardingEducationParams: OnboardingEducationParams) {
    OnboardingEducationContent(onboardingEducationParams = onboardingEducationParams)
}

/**
 * Displays the internal content for the Onboarding Education screen using a horizontal pager.
 * Each page presents an [EducationInfo] object (title, description, image, and button text).
 *
 * The pager includes:
 * - Navigation through pages via swipe.
 * - Final page includes a button to exit the education flow.
 * - Close icon and system back button both invoke onGotItButtonClicked
 *
 * @param onboardingEducationParams Contains the current screen state and user interaction callbacks.
 */
@Composable
private fun OnboardingEducationContent(onboardingEducationParams: OnboardingEducationParams) {
    val pagerState = rememberPagerState { onboardingEducationParams.state.educationInfoList.size }

    BackHandler(enabled = true) {
        onboardingEducationParams.onGotItButtonClicked.invoke()
    }

    EducationPager(
        items = onboardingEducationParams.state.educationInfoList,
        pagerState = pagerState,
        pageContent = { page ->
            EducationScreen(
                educationInfo = page,
                pagerState = pagerState,
                nextPage = (pagerState.currentPage + 1)
                    .coerceAtMost(onboardingEducationParams.state.educationInfoList.lastIndex),
                onButtonClicked = if (pagerState.currentPage == 2) {
                    onboardingEducationParams.onGotItButtonClicked
                } else {
                    null
                },
                onCloseIconClicked = onboardingEducationParams.onGotItButtonClicked
            )
        }
    )
}

/**
 * Preview of the Onboarding Education screen with a single placeholder page.
 */
@Preview
@Composable
fun OnboardingEducationScreenPreview() {
    Column(modifier = Modifier.background(AppColors.White)) {
        OnboardingEducationScreen(
            onboardingEducationParams = OnboardingEducationParams(
                onGotItButtonClicked = {},
                state = OnboardingEducationState(
                    educationInfoList = listOf(
                        EducationInfo(
                            title = "This is the title",
                            description = "Lorem ipsum odor amet, consectetuer adipiscing elit. Sit nostra facilisis euismod; placerat pharetra nostra rhoncus nisi sagittis? Porttitor mattis vitae congue dignissim mus imperdiet. Commodo habitasse euismod aptent ipsum vestibulum odio aenean pulvinar. Maximus nisl metus libero eros quam faucibus et.",
                            drawableResId = DrawablesIds.placeholder,
                            buttonText = "Next"
                        )
                    )
                )
            )
        )
    }
}

package com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationPager
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationScreen
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo

@Composable
fun OnboardingEducationScreen(onboardingEducationParams: OnboardingEducationParams) {
    OnboardingEducationContent(onboardingEducationParams = onboardingEducationParams)
}

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
                nextPage = (pagerState.currentPage + 1).coerceAtMost(maximumValue = onboardingEducationParams.state.educationInfoList.size - 1),
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

@Preview
@Composable
fun OnboardingEducationScreenPreview() {
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

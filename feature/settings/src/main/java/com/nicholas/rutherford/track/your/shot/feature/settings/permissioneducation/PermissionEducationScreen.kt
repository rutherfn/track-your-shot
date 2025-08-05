package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationPager
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationScreen
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo

/**
 * Displays the Permission Education screen, which presents a series of
 * informational pages explaining the need for permissions.
 *
 * This screen uses a horizontal pager to allow users to swipe through
 * the provided list of [EducationInfo] items.
 *
 * @param permissionEducationParams Contains the state and callbacks needed
 * to render and interact with the screen.
 */
@Composable
fun PermissionEducationScreen(permissionEducationParams: PermissionEducationParams) {
    val pagerState = rememberPagerState { permissionEducationParams.state.educationInfoList.size }

    BackHandler(enabled = true) { permissionEducationParams.onGotItButtonClicked.invoke() }

    EducationPager(
        items = permissionEducationParams.state.educationInfoList,
        pagerState = pagerState,
        pageContent = { page ->
            EducationScreen(
                educationInfo = page,
                pagerState = pagerState,
                nextPage = (pagerState.currentPage + 1).coerceAtMost(
                    maximumValue = permissionEducationParams.state.educationInfoList.size - 1
                ),
                onButtonClicked = if (pagerState.currentPage == 0) {
                    permissionEducationParams.onGotItButtonClicked
                } else {
                    null
                },
                onMoreInfoClicked = permissionEducationParams.onMoreInfoClicked,
                onCloseIconClicked = permissionEducationParams.onGotItButtonClicked
            )
        }
    )
}

/**
 * Preview of the [PermissionEducationScreen] with a single static education item.
 */
@Preview
@Composable
fun PermissionEducationScreenPreview() {
    PermissionEducationScreen(
        permissionEducationParams = PermissionEducationParams(
            onGotItButtonClicked = {},
            onMoreInfoClicked = {},
            state = PermissionEducationState(
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


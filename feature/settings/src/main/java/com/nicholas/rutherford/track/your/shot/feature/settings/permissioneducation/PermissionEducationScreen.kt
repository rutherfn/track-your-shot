package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationPager
import com.nicholas.rutherford.track.your.shot.compose.components.education.EducationScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PermissionEducationScreen(permissionEducationParams: PermissionEducationParams) {
    val pagerState = rememberPagerState { permissionEducationParams.state.educationInfoList.size }

    Content(
        ui = {
            EducationPager(
                items = permissionEducationParams.state.educationInfoList,
                pagerState = pagerState,
                pageContent =  { page ->
                    EducationScreen(
                        educationInfo = page,
                        pagerState = pagerState,
                        nextPage = (pagerState.currentPage + 1).coerceAtMost(maximumValue = permissionEducationParams.state.educationInfoList.size - 1),
                        onButtonClicked =  if (pagerState.currentPage == 1) {
                            permissionEducationParams.onGotItButtonClicked
                        } else {
                            null
                        }
                    )
                }
            )
        }
    )
}
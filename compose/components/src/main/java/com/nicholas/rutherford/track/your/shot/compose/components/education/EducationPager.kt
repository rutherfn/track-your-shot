package com.nicholas.rutherford.track.your.shot.compose.components.education

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds

/**
 * Default [HorizontalPager] that load items from [EducationInfo]
 * used for Education screens found in the app
 *
 * @param items required param - list of [EducationInfo] responsible for displaying view content
 * @param modifier optional param - Sets the [Modifier] of the [HorizontalPager]
 * @param pagerState optional param - Sets the pager that is used with the [HorizontalPager]
 * @param pageContent required param - Allows us to pass in a [Composable] with [EducationInfo]
 * as a param, for a given page used in [HorizontalPager]
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EducationPager(
    items: List<EducationInfo>,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState { items.size },
    pageContent: @Composable (EducationInfo) -> Unit
) {
    HorizontalPager(
        modifier = modifier.fillMaxSize(),
        state = pagerState
    ) { page ->
        pageContent(items[page])
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun EducationPagerPreview() {
        val educationInfoList = listOf(
            EducationInfo(
                title = "This is the title",
                description = "Lorem ipsum odor amet, consectetuer adipiscing elit. Sit nostra facilisis euismod; placerat pharetra nostra rhoncus nisi sagittis? Porttitor mattis vitae congue dignissim mus imperdiet. Commodo habitasse euismod aptent ipsum vestibulum odio aenean pulvinar. Maximus nisl metus libero eros quam faucibus et.",
                drawableResId = DrawablesIds.placeholder,
                buttonText = "Next"
            )
        )
        val pagerState = rememberPagerState { educationInfoList.size }

        EducationPager(
            items = educationInfoList,
            modifier = Modifier.background(Color.White),
            pageContent = { page ->
                EducationScreen(
                    educationInfo = page,
                    pagerState = pagerState,
                    nextPage = 2
                )
            }
        )
}

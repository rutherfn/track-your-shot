package com.nicholas.rutherford.track.your.shot.compose.components.education

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Page Indicator that uses [pagerState] to show how many indicators as well as
 * what pages are active vus inactive in the indicator list
 *
 * @param pagerState = [PagerState] pager to use for looping through how many indicators to show
 * as well as setting up state of which one should be active or not.
 */
@Composable
fun BoxScope.EducationPageIndicator(pagerState: PagerState) {
    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) {
                AppColors.Orange
            } else {
                Color.LightGray
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(16.dp)
            )
        }
    }
}

/**
 * Preview for [EducationPageIndicator] showing 5 pages with the 2nd page active.
 */
@OptIn(ExperimentalCoroutinesApi::class, ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun EducationPageIndicatorPreview() {
    // Create a sample PagerState with 5 pages
    val pagerState = rememberPagerState { (0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        EducationPageIndicator(pagerState = pagerState)
    }
}

package com.nicholas.rutherford.track.your.shot.compose.components.education

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds

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
    Column(modifier = Modifier.background(Color.White)) {
    EducationPager(
        items = listOf(
            EducationInfo(
                title = "Test",
                description = "Description",
                drawableResId = DrawablesIds.placeholder,
                buttonText = "Next"
            )
        ),
        modifier = Modifier,
        pageContent = { page ->
            Text(text = page.title)
        }
    )
    }
}
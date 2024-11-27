package com.nicholas.rutherford.track.your.shot.compose.components.education

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.launch

/**
 * Education screen gets used in [EducationPager] to load screen content for Pager.
 *
 * @param educationInfo [EducationInfo] info that gets loaded in the content for the given screen
 * @param pagerState [PagerState] handles pager view that gets passed down from [EducationPager]
 * @param nextPage [Int] defines the next page we want to go to in the [PagerState]
 * @param modifier [Modifier] sets the [Modifier] that the [Column] wraps inside of view
 * @param onButtonClicked [Unit] function that gets invoked when button is clicked
 * @param onMoreInfoClicked [Unit] function that gets invoked when text of More Info is clicked
 * @param onCloseIconClicked [Unit] function that gets invoked when we click close icon button
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EducationScreen(
    educationInfo: EducationInfo,
    pagerState: PagerState,
    nextPage: Int,
    modifier: Modifier = Modifier,
    onButtonClicked: (() -> Unit)? = null,
    onMoreInfoClicked: (() -> Unit)? = null,
    onCloseIconClicked: (() -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var buttonEnabled by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = educationInfo.drawableResId),
                contentDescription = "Education Image",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = educationInfo.title,
                textAlign = TextAlign.Center,
                style = TextStyles.medium,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = educationInfo.description,
                textAlign = TextAlign.Center,
                style = TextStyles.small,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            if (educationInfo.moreInfoVisible) {
                Text(
                    text = stringResource(id = StringsIds.moreInfo),
                    textAlign = TextAlign.Center,
                    style = TextStyles.hyperLink.copy(fontSize = 16.sp, color = Color.Blue),
                    modifier = Modifier.padding(8.dp).clickable { onMoreInfoClicked?.invoke() }
                )
            }

            Button(
                onClick = {
                    onButtonClicked?.invoke() ?: run {
                        buttonEnabled = false
                        if (nextPage < pagerState.pageCount) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(nextPage)
                                buttonEnabled = true
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(size = 50.dp),
                enabled = buttonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Padding.twelve),
                colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor),
                content = {
                    Text(
                        text = educationInfo.buttonText,
                        style = TextStyles.small,
                        color = Color.White
                    )
                }
            )
        }
        IconButton(
            onClick = { onCloseIconClicked?.invoke() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Icon",
                tint = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun EducationScreenPreview() {
    val educationInfoList = listOf(
        EducationInfo(
            title = "This is the title",
            description = "Lorem ipsum odor amet, consectetuer adipiscing elit. Sit nostra facilisis euismod; placerat pharetra nostra rhoncus nisi sagittis? Porttitor mattis vitae congue dignissim mus imperdiet. Commodo habitasse euismod aptent ipsum vestibulum odio aenean pulvinar. Maximus nisl metus libero eros quam faucibus et.",
            drawableResId = DrawablesIds.placeholder,
            buttonText = "Next"
        )
    )
    val pagerState = rememberPagerState { educationInfoList.size }
    EducationScreen(
        educationInfo = educationInfoList.first(),
        pagerState = pagerState,
        nextPage = 2,
        modifier = Modifier.background(Color.White),
        onButtonClicked = null
    )
}

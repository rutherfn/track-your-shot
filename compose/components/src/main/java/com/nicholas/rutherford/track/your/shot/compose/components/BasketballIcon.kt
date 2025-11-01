package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds

/**
 * Created by Nicholas Rutherford, last edited on 2025-01-XX
 *
 * A basketball icon component that displays a basketball image in a circular frame.
 * Uses an actual basketball image from drawable resources for a realistic appearance.
 *
 * @param size The size of the basketball icon (default: 80.dp)
 * @param modifier Optional modifier to apply to the basketball
 * @param drawableId Optional drawable resource ID for the basketball image.
 *                   Defaults to [DrawablesIds.sportsBasketball].
 *                   You can add a real basketball PNG image to drawables and pass its ID here.
 */
@Composable
fun BasketballIcon(
    size: Dp = 80.dp,
    modifier: Modifier = Modifier,
    drawableId: Int = DrawablesIds.sportsBasketball
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Basketball",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BasketballIconPreview() {
    BasketballIcon(size = 80.dp)
}

@Preview(showBackground = true)
@Composable
private fun BasketballIconSmallPreview() {
    BasketballIcon(size = 40.dp)
}

@Preview(showBackground = true)
@Composable
private fun BasketballIconLargePreview() {
    BasketballIcon(size = 120.dp)
}

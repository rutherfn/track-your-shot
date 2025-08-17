package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * Composable for uploading and displaying a player's profile image.
 *
 * Displays a circular image area that either shows:
 * - the uploaded image,
 * - an existing image URL, or
 * - a placeholder camera icon.
 *
 * Tapping the image triggers the image upload logic and expands the bottom sheet.
 *
 * @param hasUploadedImage Flag indicating if a new image has been uploaded.
 * @param scope Coroutine scope used to launch the bottom sheet.
 * @param bottomState The bottom sheet state to be shown when the image is clicked.
 * @param createEditPlayerParams Contains the player's edit state and callback for upload click.
 * @param imageUri Local image URI selected by the user, if available.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPlayerImageContent(
    hasUploadedImage: Boolean,
    scope: CoroutineScope,
    bottomState: SheetState,
    createEditPlayerParams: CreateEditPlayerParams,
    imageUri: Uri?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Upload Player Image",
            style = TextStyles.body,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start)
        )

        Surface(
            shape = CircleShape,
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
            color = AppColors.White,
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    createEditPlayerParams.onImageUploadClicked.invoke(imageUri)
                    scope.launch { bottomState.show() }
                }
        ) {
            if ((hasUploadedImage && imageUri != null) || createEditPlayerParams.state.editedPlayerUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUri ?: createEditPlayerParams.state.editedPlayerUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Add photo",
                        tint = AppColors.Black.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tap To Upload",
            style = TextStyles.body,
            color = AppColors.Black.copy(alpha = 0.6f)
        )
    }
}

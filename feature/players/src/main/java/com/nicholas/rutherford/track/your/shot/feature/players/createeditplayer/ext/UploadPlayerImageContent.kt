package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPlayerImageContent(
    hasUploadedImage: Boolean,
    scope: CoroutineScope,
    bottomState: SheetState,
    createEditPlayerParams: CreateEditPlayerParams,
    imageUri: Uri?
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (hasUploadedImage && imageUri != null || createEditPlayerParams.state.editedPlayerUrl.isNotEmpty()) {
            AsyncImage(
                model = imageUri ?: createEditPlayerParams.state.editedPlayerUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clickable {
                        createEditPlayerParams.onImageUploadClicked.invoke(imageUri)
                        scope.launch { bottomState.show() }
                    }
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = "Add a photo icon",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        createEditPlayerParams.onImageUploadClicked.invoke(imageUri)
                        scope.launch { bottomState.show() }
                    }
            )
        }
    }
}

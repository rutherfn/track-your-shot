package com.nicholas.rutherford.track.your.shot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerParams
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerScreen
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerViewModel

class ScreenContents {

    fun createEditPlayerContent(
        firstNameArgument: String?,
        lastNameArgument: String?,
        createEditPlayerViewModel: CreateEditPlayerViewModel
    ): @Composable (NavBackStackEntry) -> Unit = {
        CreateEditPlayerScreen(
            createEditPlayerParams = CreateEditPlayerParams(
                state = createEditPlayerViewModel.createEditPlayerStateFlow.collectAsState().value,
                onClearImageState = { createEditPlayerViewModel.onClearImageState() },
                checkForExistingPlayer = {
                    createEditPlayerViewModel.checkForExistingPlayer(
                        firstNameArgument = firstNameArgument,
                        lastNameArgument = lastNameArgument
                    )
                },
                onToolbarMenuClicked = { createEditPlayerViewModel.onToolbarMenuClicked() },
                onLogShotsClicked = { createEditPlayerViewModel.onLogShotsClicked() },
                onFirstNameValueChanged = { newFirstName ->
                    createEditPlayerViewModel.onFirstNameValueChanged(
                        newFirstName = newFirstName
                    )
                },
                onLastNameValueChanged = { newLastName ->
                    createEditPlayerViewModel.onLastNameValueChanged(
                        newLastName = newLastName
                    )
                },
                onPlayerPositionStringChanged = { newPositionStringResId ->
                    createEditPlayerViewModel.onPlayerPositionStringChanged(
                        newPositionStringResId
                    )
                },
                onImageUploadClicked = { uri -> createEditPlayerViewModel.onImageUploadClicked(uri) },
                onCreatePlayerClicked = { uri -> createEditPlayerViewModel.onCreatePlayerClicked(uri) },
                permissionNotGrantedForCameraAlert = { createEditPlayerViewModel.permissionNotGrantedForCameraAlert() },
                permissionNotGrantedForReadMediaOrExternalStorageAlert = { createEditPlayerViewModel.permissionNotGrantedForReadMediaOrExternalStorageAlert() },
                onSelectedCreateEditImageOption = { option ->
                    createEditPlayerViewModel.onSelectedCreateEditImageOption(option)
                },
                onViewShotClicked = { shotType, shotId ->
                    createEditPlayerViewModel.onViewShotClicked(shotType = shotType, shotId = shotId)
                },
                onViewPendingShotClicked = { shotType, shotId ->
                    createEditPlayerViewModel.onViewPendingShotClicked(shotType = shotType, shotId = shotId)
                }
            )
        )
    }
}

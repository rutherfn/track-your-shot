package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.BottomSheetWithOptions
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.PositionChooser
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.ShotsContent
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.UploadPlayerImageContent
import com.nicholas.rutherford.track.your.shot.helper.extensions.getImageUri
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasCameraPermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the Create/Edit Player screen.
 *
 * Composable screen used to either create a new player or edit an existing player.
 *
 * It provides UI for inputting a player's general information, selecting their position, uploading
 * an image, and logging/viewing shots. Also handles user interactions such as selecting images
 * from camera or gallery and showing a bottom sheet for image upload options.
 *
 * @param createEditPlayerParams All state and callback parameters required to drive the screen.
 */
@Composable
fun CreateEditPlayerScreen(createEditPlayerParams: CreateEditPlayerParams) {
    BackHandler(true) {
        createEditPlayerParams.onToolbarMenuClicked()
    }

    CreateEditPlayerContent(createEditPlayerParams = createEditPlayerParams)
}

/**
 * Core content of the Create/Edit Player screen that manages camera/gallery launchers,
 * permission requests, and the bottom sheet for image actions.
 *
 * @param createEditPlayerParams All state and callback parameters for managing player creation/editing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditPlayerContent(createEditPlayerParams: CreateEditPlayerParams) {
    val bottomState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var hasUploadedImage by remember { mutableStateOf(false) }
    var shouldAskForCameraPermission by remember { mutableStateOf(value = false) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        hasUploadedImage = bitmap != null || createEditPlayerParams.state.imageUri != null
        createEditPlayerParams.updateImageUriState.invoke(
            bitmap?.let { getImageUri(context = context, image = it) } ?: run { null }
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                cameraLauncher.launch()
            } else {
                createEditPlayerParams.permissionNotGrantedForCameraAlert.invoke()
            }
        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            hasUploadedImage = uri != null || createEditPlayerParams.state.imageUri != null
            createEditPlayerParams.updateImageUriState.invoke(uri)
        }
    )

    LaunchedEffect(shouldAskForCameraPermission) {
        if (shouldAskForCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            shouldAskForCameraPermission = false
        }
    }

    BottomSheetWithOptions(
        sheetState = bottomState,
        sheetInfo = createEditPlayerParams.state.sheet,
        onSheetItemClicked = { value, _ ->
            scope.launch { bottomState.hide() }
            handleBottomSheetSelection(
                value = value,
                context = context,
                cameraLauncher = cameraLauncher,
                singlePhotoPickerLauncher = singlePhotoPickerLauncher,
                createEditPlayerParams = createEditPlayerParams,
                shouldAskForCameraPermission = { shouldAskForCameraPermission = it },
                resetImageState = {
                    createEditPlayerParams.updateImageUriState.invoke(null)
                    hasUploadedImage = false
                    createEditPlayerParams.onClearImageState()
                }
            )
        },
        onCancelItemClicked = { scope.launch { bottomState.hide() } },
        content = {
            CreateEditPlayerUi(
                createEditPlayerParams = createEditPlayerParams,
                hasUploadedImage = hasUploadedImage,
                scope = scope,
                bottomState = bottomState,
                imageUri = createEditPlayerParams.state.imageUri
            )
        }
    )
}

/**
 * Handles selection from the image picker bottom sheet.
 *
 * @param value The selected item string.
 * @param context The current Android context.
 * @param cameraLauncher Launcher for taking a picture with the camera.
 * @param singlePhotoPickerLauncher Launcher for picking an image from the gallery.
 * @param createEditPlayerParams Parameter holder with UI and callback state.
 * @param shouldAskForCameraPermission Callback to trigger camera permission request.
 * @param resetImageState Callback to clear the current image state and reset upload state.
 */
private fun handleBottomSheetSelection(
    value: String,
    context: Context,
    cameraLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>,
    singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    createEditPlayerParams: CreateEditPlayerParams,
    shouldAskForCameraPermission: (Boolean) -> Unit,
    resetImageState: () -> Unit
) {
    when (createEditPlayerParams.onSelectedCreateEditImageOption(value)) {
        CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY -> {
            singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        CreateEditImageOption.TAKE_A_PICTURE -> {
            if (hasCameraPermissionEnabled(context)) {
                cameraLauncher.launch()
            } else {
                shouldAskForCameraPermission(true)
            }
        }
        else -> resetImageState()
    }
}

/**
 * UI layout for the Create/Edit Player form. Includes fields for name input, position selection,
 * image upload, and logged/pending shots display.
 *
 * @param createEditPlayerParams Parameters and state used to bind UI to the ViewModel.
 * @param hasUploadedImage Boolean flag to indicate if an image has been uploaded.
 * @param scope Coroutine scope used for launching sheet-related actions.
 * @param bottomState Sheet state for controlling the image options bottom sheet.
 * @param imageUri The current image URI if an image is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateEditPlayerUi(
    createEditPlayerParams: CreateEditPlayerParams,
    hasUploadedImage: Boolean,
    scope: CoroutineScope,
    bottomState: SheetState,
    imageUri: Uri?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(AppColors.White)
            .padding(
                start = Padding.twenty,
                end = Padding.twenty,
                bottom = Padding.twenty
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.general_information),
            style = TextStyles.small,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = Padding.twelve, start = Padding.four)
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        CoreTextField(
            value = createEditPlayerParams.state.firstName,
            onValueChange = {
                createEditPlayerParams.onFirstNameValueChanged.invoke(it)
            },
            placeholderValue = stringResource(id = R.string.enter_first_name)
        )

        Spacer(modifier = Modifier.height(Padding.sixteen))

        CoreTextField(
            value = createEditPlayerParams.state.lastName,
            onValueChange = {
                createEditPlayerParams.onLastNameValueChanged.invoke(it)
            },
            placeholderValue = stringResource(id = R.string.enter_last_name)
        )

        Spacer(modifier = Modifier.height(Padding.sixteen))

        PositionChooser(onPlayerPositionStringChanged = createEditPlayerParams.onPlayerPositionStringChanged)

        Spacer(modifier = Modifier.height(Padding.sixteen))

        UploadPlayerImageContent(
            hasUploadedImage = hasUploadedImage,
            scope = scope,
            bottomState = bottomState,
            createEditPlayerParams = createEditPlayerParams,
            imageUri = imageUri
        )

        Spacer(modifier = Modifier.height(Padding.sixteen))

        ShotsContent(
            shotList = createEditPlayerParams.state.shots,
            pendingShotList = createEditPlayerParams.state.pendingShots,
            hintLogNewShotText = createEditPlayerParams.state.hintLogNewShotText,
            onLogShotsClicked = createEditPlayerParams.onLogShotsClicked,
            onViewShotClicked = { shotType, shotId ->
                createEditPlayerParams.onViewShotClicked.invoke(shotType, shotId)
            },
            onPendingShotClicked = { shotType, shotId ->
                createEditPlayerParams.onViewPendingShotClicked.invoke(shotType, shotId)
            }
        )
    }
}

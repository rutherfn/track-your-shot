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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
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
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.PositionChooser
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.ShotsContent
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.UploadPlayerImageContent
import com.nicholas.rutherford.track.your.shot.helper.extensions.getImageUri
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasCameraPermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateEditPlayerScreen(createEditPlayerParams: CreateEditPlayerParams) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var hasUploadedImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var shouldAskForCameraPermission by remember { mutableStateOf(value = false) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        hasUploadedImage = bitmap != null || imageUri != null
        imageUri = bitmap?.let { getImageUri(context = context, image = it) }
            ?: imageUri
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
            hasUploadedImage = uri != null || imageUri != null
            imageUri = uri ?: imageUri
        }
    )

    if (shouldAskForCameraPermission) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        shouldAskForCameraPermission = false
    }

    BackHandler(true) {
        createEditPlayerParams.onToolbarMenuClicked()
    }

    LaunchedEffect(Unit) {
        createEditPlayerParams.checkForExistingPlayer()
    }

    Content(
        ui = {
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
                            imageUri = null
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
                        imageUri = imageUri
                    )
                }

            )
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = createEditPlayerParams.state.toolbarNameResId),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = {
                createEditPlayerParams.onToolbarMenuClicked.invoke()
            },
            onSecondaryIconButtonClicked = {
                createEditPlayerParams.onCreatePlayerClicked.invoke(imageUri)
            },
        ),
        secondaryIconTint = AppColors.White
    )
}

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CreateEditPlayerUi(
    createEditPlayerParams: CreateEditPlayerParams,
    hasUploadedImage: Boolean,
    scope: CoroutineScope,
    bottomState: ModalBottomSheetState,
    imageUri: Uri?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

        PositionChooser(createEditPlayerParams = createEditPlayerParams)

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

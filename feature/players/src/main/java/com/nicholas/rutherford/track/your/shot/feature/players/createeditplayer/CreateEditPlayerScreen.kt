@file:OptIn(ExperimentalMaterialApi::class)

package com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
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
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.LogShotsContent
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.PositionChooser
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.ext.UploadPlayerImageContent
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.getImageUri
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasCameraPermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.extensions.hasReadImagePermissionEnabled
import com.nicholas.rutherford.track.your.shot.helper.extensions.readMediaImagesOrExternalStoragePermission
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreatePlayerScreen(createEditPlayerParams: CreateEditPlayerParams) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var hasUploadedImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var shouldAskForCameraPermission by remember { mutableStateOf(value = false) }
    var shouldAskGalleryPermission by remember { mutableStateOf(value = false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        createEditPlayerParams.checkForExistingPlayer()
    }

    Content(
        ui = {
            ModalBottomSheetLayout(
                sheetState = bottomState,
                sheetContent = {
                    createEditPlayerParams.state.sheet?.let { sheet ->
                        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent(),
                            onResult = { uri ->
                                hasUploadedImage = uri != null || imageUri != null
                                imageUri = uri ?: imageUri
                            }
                        )
                        val cameraLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.TakePicturePreview(),
                            onResult = { bitmap ->
                                hasUploadedImage = bitmap != null || imageUri != null
                                imageUri = bitmap?.let { getImageUri(context = context, image = it) } ?: imageUri
                            }
                        )
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
                        val readStorageOrMediaImagesPermissionLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission(),
                            onResult = { isGranted ->
                                if (isGranted) {
                                    singlePhotoPickerLauncher.launch(Constants.IMAGE)
                                } else {
                                    createEditPlayerParams.permissionNotGrantedForReadMediaOrExternalStorageAlert.invoke()
                                }
                            }
                        )

                        if (shouldAskForCameraPermission) {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            shouldAskForCameraPermission = false
                        }

                        if (shouldAskGalleryPermission) {
                            readStorageOrMediaImagesPermissionLauncher.launch(readMediaImagesOrExternalStoragePermission())
                            shouldAskGalleryPermission = false
                        }

                        Text(
                            text = sheet.title,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
                            style = TextStyles.smallBold
                        )
                        LazyColumn {
                            items(sheet.values) { value ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp, horizontal = 20.dp)
                                        .clickable {
                                            scope.launch { bottomState.hide() }

                                            when (createEditPlayerParams.onSelectedCreateEditImageOption(value)) {
                                                CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY -> {
                                                    if (hasReadImagePermissionEnabled(context = context)) {
                                                        singlePhotoPickerLauncher.launch(Constants.IMAGE)
                                                    } else {
                                                        shouldAskGalleryPermission = true
                                                    }
                                                }

                                                CreateEditImageOption.TAKE_A_PICTURE -> {
                                                    if (hasCameraPermissionEnabled(context = context)) {
                                                        cameraLauncher.launch()
                                                    } else {
                                                        shouldAskForCameraPermission = true
                                                    }
                                                }

                                                else -> {
                                                    imageUri = null
                                                    hasUploadedImage = false
                                                }
                                            }
                                        }
                                ) {
                                    Text(
                                        text = value,
                                        modifier = Modifier.padding(end = 20.dp),
                                        style = TextStyles.body
                                    )
                                }
                            }
                        }
                        Text(
                            text = stringResource(id = R.string.cancel),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 20.dp)
                                .clickable { scope.launch { bottomState.hide() } },
                            style = TextStyles.body.copy(color = AppColors.Red)
                        )
                    }
                }
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

                    LogShotsContent(
                        firstName = createEditPlayerParams.state.firstName,
                        lastName = createEditPlayerParams.state.lastName
                    )
                }
            }
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.create_player),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = true,
            onIconButtonClicked = {
                createEditPlayerParams.onToolbarMenuClicked.invoke()
            },
            onSecondaryIconButtonClicked = {
                createEditPlayerParams.onCreatePlayerClicked.invoke(imageUri)
            }
        )
    )
}

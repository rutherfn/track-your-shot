@file:OptIn(ExperimentalMaterialApi::class)

package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.Colors
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.getImageUri
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreatePlayerScreen(createPlayerParams: CreatePlayerParams) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var hasUploadedImage by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var shouldAskForCameraPermission by remember { mutableStateOf(value = false) }
    val context = LocalContext.current

    Content(
        ui = {
            ModalBottomSheetLayout(
                sheetState = bottomState,
                sheetContent = {
                    createPlayerParams.state.sheet?.let { sheet ->
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
                                    singlePhotoPickerLauncher.launch(Constants.IMAGE)
                                } else {
                                    createPlayerParams.permissionNotGrantedForCameraAlert.invoke()
                                }
                            }
                        )

                        if (shouldAskForCameraPermission) {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            shouldAskForCameraPermission = false
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

                                            when (createPlayerParams.onSelectedCreateEditImageOption(
                                                value
                                            )) {
                                                CreateEditImageOption.CHOOSE_IMAGE_FROM_GALLERY -> {
                                                    if (ContextCompat.checkSelfPermission(
                                                            context,
                                                            Manifest.permission.CAMERA
                                                        ) == PackageManager.PERMISSION_GRANTED
                                                    ) {
                                                        singlePhotoPickerLauncher.launch(Constants.IMAGE)
                                                    } else {
                                                        // get here
                                                    }
                                                }

                                                CreateEditImageOption.TAKE_A_PICTURE -> {
                                                    if (ContextCompat.checkSelfPermission(
                                                            context,
                                                            Manifest.permission.CAMERA
                                                        ) == PackageManager.PERMISSION_GRANTED
                                                    ) {
                                                        cameraLauncher.launch()
                                                    } else {
                                                        shouldAskForCameraPermission = true
                                                    }
                                                }

                                                CreateEditImageOption.REMOVE_IMAGE -> {
                                                    imageUri = null
                                                    hasUploadedImage = false
                                                }

                                                else -> {}
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
                        value = createPlayerParams.state.firstName,
                        onValueChange = {
                            createPlayerParams.onFirstNameValueChanged.invoke(it)
                        },
                        placeholderValue = stringResource(id = R.string.enter_first_name)
                    )

                    Spacer(modifier = Modifier.height(Padding.sixteen))

                    CoreTextField(
                        value = createPlayerParams.state.lastName,
                        onValueChange = {
                            createPlayerParams.onLastNameValueChanged.invoke(it)
                        },
                        placeholderValue = stringResource(id = R.string.enter_last_name)
                    )

                    Spacer(modifier = Modifier.height(Padding.sixteen))

                    PositionChooser(createPlayerParams = createPlayerParams)

                    Spacer(modifier = Modifier.height(Padding.sixteen))

                    UploadPlayerImageContent(
                        hasUploadedImage = hasUploadedImage,
                        scope = scope,
                        bottomState = bottomState,
                        createPlayerParams = createPlayerParams,
                        imageUri = imageUri
                    )

                    Spacer(modifier = Modifier.height(Padding.sixteen))

                    LogShotsContent(
                        firstName = createPlayerParams.state.firstName,
                        lastName = createPlayerParams.state.lastName
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
                createPlayerParams.onToolbarMenuClicked.invoke()
            },
            onSecondaryIconButtonClicked = {
                createPlayerParams.onCreatePlayerClicked.invoke(imageUri)
            }
        )
    )
}

@Composable
private fun UploadPlayerImageContent(
    hasUploadedImage: Boolean,
    scope: CoroutineScope,
    bottomState: ModalBottomSheetState,
    createPlayerParams: CreatePlayerParams,
    imageUri: Uri?
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (hasUploadedImage && imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clickable {
                        createPlayerParams.onImageUploadClicked.invoke(imageUri)
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
                        createPlayerParams.onImageUploadClicked.invoke(imageUri)
                        scope.launch { bottomState.show() }
                    }
            )
        }
    }
}

@Composable
private fun ColumnScope.LogShotsContent(
    isEmpty: Boolean = true,
    firstName: String,
    lastName: String
) {
    val hintLogNewShot = if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
        stringResource(id = R.string.hint_log_new_shots_for_player_x, "$firstName $lastName")
    } else if (firstName.isNotEmpty()) {
        stringResource(id = R.string.hint_log_new_shots_for_player_x, firstName)
    } else if (lastName.isNotEmpty()) {
        stringResource(id = R.string.hint_log_new_shots_for_player_x, lastName)
    } else {
        stringResource(id = StringsIds.hintLogNewShots)
    }

    Text(
        text = stringResource(id = R.string.log_shots),
        style = TextStyles.small,
        modifier = Modifier
            .align(Alignment.Start)
            .padding(top = Padding.twelve, start = Padding.four)
    )

    if (isEmpty) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_basketball_log_shot_empty_state),
                    contentDescription = null,
                    modifier = Modifier.size(90.dp)
                )

                Text(
                    text = stringResource(id = StringsIds.noCurrentShotsLoggedForPlayer),
                    style = TextStyles.smallBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = hintLogNewShot,
                    style = TextStyles.small,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Button(
                    onClick = {
                        // todo -> take user to list to log new shots
                    },
                    shape = RoundedCornerShape(size = 50.dp),
                    modifier = Modifier
                        .padding(vertical = Padding.twelve)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor)
                ) {
                    Text(
                        text = "Log Shots",
                        style = TextStyles.smallBold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    } else {
        // todo -> show list of shots users logged
    }
}

@Composable
private fun PositionChooser(createPlayerParams: CreatePlayerParams) {
    val currentPlayerPosition = stringResource(id = createPlayerParams.state.playerPositionStringResId)
    var selectedOption by remember { mutableStateOf(value = currentPlayerPosition) }
    var isDropdownExpanded by remember { mutableStateOf(value = false) }

    val options = listOf(
        stringResource(id = R.string.point_guard),
        stringResource(id = R.string.shooting_guard),
        stringResource(id = R.string.small_forward),
        stringResource(id = R.string.power_forward),
        stringResource(id = R.string.center)
    )

    Box {
        Spacer(modifier = Modifier.height(Padding.sixteen))
        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        selectedOption = option
                        isDropdownExpanded = false
                    }
                ) {
                    Text(
                        text = option,
                        style = TextStyles.body
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.position),
                modifier = Modifier.padding(start = Padding.four),
                style = TextStyles.body
            )
            Spacer(modifier = Modifier.height(Padding.eight))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier
                        .padding(start = Padding.four)
                        .clickable { isDropdownExpanded = !isDropdownExpanded },
                    style = TextStyles.body
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "2",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                        .clickable { isDropdownExpanded = !isDropdownExpanded }
                )
            }
        }
    }
}

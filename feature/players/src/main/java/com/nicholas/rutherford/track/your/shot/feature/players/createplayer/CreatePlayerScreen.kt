@file:OptIn(ExperimentalMaterialApi::class)

package com.nicholas.rutherford.track.your.shot.feature.players.createplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.TextFieldNoPadding
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import kotlinx.coroutines.launch

@Composable
fun CreatePlayerScreen(createPlayerParams: CreatePlayerParams) {
    Content(
        ui = {
            CreatePlayerScreenContent(createPlayerParams = createPlayerParams)
             },
        appBar = AppBar(
            toolbarTitle = stringResource(id = R.string.create_player),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            onIconButtonClicked = {
                createPlayerParams.onToolbarMenuClicked.invoke()
            }
        )
    )
}

@Composable
private fun CreatePlayerScreenContent(createPlayerParams: CreatePlayerParams) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetContent = {
            createPlayerParams.state.sheet?.let {
                BottomSheetContent(it.title, it.values)
            }
        }
    ) {

        Text(
            text = stringResource(id = R.string.add_player_info),
            modifier = Modifier.padding(top = Padding.eight, start = Padding.twenty),
            style = TextStyles.smallBold
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = Padding.twenty, end = Padding.twenty, bottom = Padding.twenty),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldNoPadding(
                label = stringResource(id = StringsIds.firstName),
                value = createPlayerParams.state.firstName,
                onValueChange = { newFirstName ->
                    createPlayerParams.onFirstNameValueChanged.invoke(newFirstName)
                }
            )

            Spacer(modifier = Modifier.height(Padding.four))

            TextFieldNoPadding(
                label = stringResource(id = StringsIds.lastName),
                value = createPlayerParams.state.lastName,
                onValueChange = { newLastName ->
                    createPlayerParams.onLastNameValueChanged.invoke(newLastName)
                }
            )

            Spacer(modifier = Modifier.height(Padding.sixteen))

            PositionChooser(createPlayerParams = createPlayerParams)

            Spacer(modifier = Modifier.height(Padding.sixteen))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = StringsIds.uploadPlayerImage))
                Spacer(modifier = Modifier.height(Padding.eight))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    createPlayerParams.state.imageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    createPlayerParams.onImageUploadClicked.invoke()
                                    //isBottomSheetVisible = !isBottomSheetVisible
                                },
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Add a photo",
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    createPlayerParams.onImageUploadClicked.invoke()
                                    scope.launch { bottomState.show() }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomSheetContent(title: String, values: List<String>) {
    Text(
        text = title,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
        style = TextStyles.smallBold
    )
    LazyColumn {
        items(values) { value ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 20.dp)
                    .clickable { }
            ) {
                Text(
                    text = value,
                    modifier = Modifier.padding(end = 20.dp),
                    style = TextStyles.body
                )
            }
        }
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
                    Text(text = option)
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.position))
            Spacer(modifier = Modifier.height(Padding.eight))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    modifier = Modifier.clickable { isDropdownExpanded = !isDropdownExpanded }
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

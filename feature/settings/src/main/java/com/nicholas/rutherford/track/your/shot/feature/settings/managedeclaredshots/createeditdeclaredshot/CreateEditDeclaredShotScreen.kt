package com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.compose.components.CoreMultilineTextField
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

@Composable
fun CreateEditDeclaredShotScreen(params: CreateEditDeclaredShotScreenParams) {
    Content(
        ui = {
            if (params.state.declaredShotState == DeclaredShotState.VIEWING && params.state.currentDeclaredShot != null) {
                params.state.currentDeclaredShot.let { declaredShot ->
                    ViewDeclaredShot(declaredShot = declaredShot, onDeleteShotClicked = params.onDeleteShotClicked)
                }
            } else if (params.state.declaredShotState == DeclaredShotState.EDITING  && params.state.currentDeclaredShot != null) {
                params.state.currentDeclaredShot.let { declaredShot ->
                    EditDeclaredShot(
                        declaredShot = declaredShot,
                        onShotNameValueChanged = params.onShotNameValueChanged,
                        onShotCategoryValueChanged = params.onShotCategoryValueChanged,
                        onShotDescriptionValueChanged = params.onShotDescriptionValueChanged
                    )
                }
            }
        },
        appBar = AppBar(
            toolbarTitle = params.state.toolbarTitle,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() },
            shouldShowSecondaryButton = true,
            onSecondaryIconButtonClicked = {
                if (params.state.declaredShotState == DeclaredShotState.VIEWING) {
                    params.onEditShotPencilClicked.invoke()
                } else {
                    params.onEditOrCreateNewShot.invoke()
                }
            }
        ),
        secondaryImageEnabled = true,
        secondaryImageVector = if (params.state.declaredShotState == DeclaredShotState.VIEWING) {
            Icons.Default.Edit
        } else {
            Icons.Default.Save
        },
    )
}

@Composable
fun EditDeclaredShot(
    declaredShot: DeclaredShot,
    onShotNameValueChanged: (shotName: String) -> Unit,
    onShotDescriptionValueChanged: (shotDescription: String) -> Unit,
    onShotCategoryValueChanged: (shotDescription: String) -> Unit
) {
    var title by remember { mutableStateOf(declaredShot.title) }
    var category by remember { mutableStateOf(declaredShot.shotCategory) }
    var description by remember { mutableStateOf(declaredShot.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                bottom = Padding.sixteen
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = stringResource(id = StringsIds.shotName),
            style = TextStyles.smallBold
        )
        CoreTextField(
            value = title,
            onValueChange = { shotName ->
                title = shotName
                onShotNameValueChanged.invoke(shotName)
            },
            placeholderValue = ""
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(id = StringsIds.shotCategory),
            style = TextStyles.smallBold
        )
        CoreTextField(
            value = category,
            onValueChange = { shotCategory ->
                category = shotCategory
                onShotCategoryValueChanged.invoke(shotCategory)
            },
            placeholderValue = ""
        )

        Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(id = StringsIds.shotDescription),
            style = TextStyles.smallBold
        )
        CoreMultilineTextField(
            value = description,
            onValueChange = { shotDescription ->
                description = shotDescription
                onShotDescriptionValueChanged.invoke(shotDescription)
            },
            placeholderValue = ""
        )

        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = "All fields are required to update the shot details. Make sure each one is filled out before saving.",
            style = TextStyles.body,
            color = AppColors.LightGray
        )

    }
}

@Composable
fun ViewDeclaredShot(
    declaredShot: DeclaredShot,
    onDeleteShotClicked: (id: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.sixteen,
                end = Padding.sixteen,
                bottom = Padding.sixteen
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Padding.sixteen))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = StringsIds.shotName),
                style = TextStyles.smallBold,
                modifier = Modifier.alignBy(FirstBaseline)
            )

            Text(
                text = " ${declaredShot.title.replaceFirstChar { it.uppercaseChar() }}",
                style = TextStyles.body,
                modifier = Modifier.alignBy(FirstBaseline)
            )
        }

        Spacer(modifier = Modifier.height(Padding.eight))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(StringsIds.shotCategory),
                modifier = Modifier.alignBy(FirstBaseline),
                style = TextStyles.smallBold
            )

            Text(
                text = " ${declaredShot.shotCategory.replaceFirstChar { it.uppercaseChar() }}",
                modifier = Modifier.alignBy(FirstBaseline),
                style = TextStyles.body
            )
        }

        Spacer(modifier = Modifier.height(Padding.eight))

        Text(
            text = stringResource(StringsIds.shotDescription),
            style = TextStyles.smallBold
        )

        Spacer(modifier = Modifier.height(Padding.four))

        Text(
            text = " ${declaredShot.description.replaceFirstChar { it.uppercaseChar() }}",
            style = TextStyles.body
        )

        DeleteShotButton(
            id = declaredShot.id,
            onDeleteShotClicked = { onDeleteShotClicked.invoke(declaredShot.id) }
        )
    }
}

@Composable
private fun DeleteShotButton(
    id: Int,
    onDeleteShotClicked: (id: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onDeleteShotClicked.invoke(id) },
                shape = RoundedCornerShape(size = 50.dp),
                modifier = Modifier
                    .padding(vertical = Padding.twelve)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor)
            ) {
                Text(
                    text = "Delete Shot",
                    style = TextStyles.smallBold,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}

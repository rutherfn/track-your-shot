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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.CoreMultilineTextField
import com.nicholas.rutherford.track.your.shot.compose.components.CoreTextField
import com.nicholas.rutherford.track.your.shot.data.room.response.DeclaredShot
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Displays the screen for creating, editing, or viewing a declared shot.
 * Depending on the state, it shows the appropriate UI for:
 * - Viewing a existing shot
 * - Editing an existing shot.
 * - Creating a new shot.
 *
 * @param params Contains the state and callback handlers for this screen.
 */
@Composable
fun CreateEditDeclaredShotScreen(params: CreateEditDeclaredShotScreenParams) {
    if (params.state.declaredShotState == DeclaredShotState.VIEWING && params.state.currentDeclaredShot != null) {
        params.state.currentDeclaredShot.let { declaredShot ->
            ViewDeclaredShot(declaredShot = declaredShot, onDeleteShotClicked = params.onDeleteShotClicked)
        }
    } else {
        params.state.currentDeclaredShot?.let { declaredShot ->
            EditDeclaredShot(
                declaredShot = declaredShot,
                onEditShotNameValueChanged = params.onEditShotNameValueChanged,
                onEditShotCategoryValueChanged = params.onEditShotCategoryValueChanged,
                onEditShotDescriptionValueChanged = params.onEditShotDescriptionValueChanged
            )
        } ?: run {
            CreateDeclaredShot(
                onCreateShotNameValueChanged = params.onCreateShotNameValueChanged,
                onCreateShotCategoryValueChanged = params.onCreateShotCategoryValueChanged,
                onCreateShotDescriptionValueChanged = params.onCreateShotDescriptionValueChanged
            )
        }
    }
}

/**
 * Displays the UI for creating a new declared shot.
 * Provides text fields for entering the shot's name, category, and description.
 *
 * @param onCreateShotNameValueChanged Callback when the shot name is updated.
 * @param onCreateShotDescriptionValueChanged Callback when the description is updated.
 * @param onCreateShotCategoryValueChanged Callback when the category is updated.
 */
@Composable
fun CreateDeclaredShot(
    onCreateShotNameValueChanged: (shotName: String) -> Unit,
    onCreateShotDescriptionValueChanged: (shotDescription: String) -> Unit,
    onCreateShotCategoryValueChanged: (shotDescription: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = Padding.sixteen, end = Padding.sixteen, bottom = Padding.sixteen),
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
                onCreateShotNameValueChanged.invoke(shotName)
            },
            placeholderValue = stringResource(StringsIds.enterShotName)
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
                onCreateShotCategoryValueChanged.invoke(shotCategory)
            },
            placeholderValue = stringResource(StringsIds.enterShotCategory)
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
                onCreateShotDescriptionValueChanged.invoke(shotDescription)
            },
            placeholderValue = stringResource(StringsIds.enterShotDescription)
        )

        Spacer(modifier = Modifier.height(Padding.sixteen))

        Text(
            text = "All fields are required to create shot. Make sure each one is filled out before saving.",
            style = TextStyles.body,
            color = AppColors.LightGray
        )
    }
}

/**
 * Displays the UI for editing an existing declared shot.
 * Populates text fields with the current shot data and allows updating values.
 *
 * @param declaredShot The existing shot being edited.
 * @param onEditShotNameValueChanged Callback when the shot name is updated.
 * @param onEditShotDescriptionValueChanged Callback when the description is updated.
 * @param onEditShotCategoryValueChanged Callback when the category is updated.
 */
@Composable
fun EditDeclaredShot(
    declaredShot: DeclaredShot,
    onEditShotNameValueChanged: (shotName: String) -> Unit,
    onEditShotDescriptionValueChanged: (shotDescription: String) -> Unit,
    onEditShotCategoryValueChanged: (shotDescription: String) -> Unit
) {
    var title by remember { mutableStateOf(declaredShot.title) }
    var category by remember { mutableStateOf(declaredShot.shotCategory) }
    var description by remember { mutableStateOf(declaredShot.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = Padding.sixteen, end = Padding.sixteen, bottom = Padding.sixteen),
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
                onEditShotNameValueChanged.invoke(shotName)
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
                onEditShotCategoryValueChanged.invoke(shotCategory)
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
                onEditShotDescriptionValueChanged.invoke(shotDescription)
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

/**
 * Displays the UI for viewing an existing declared shot in a read-only format.
 * Shows shot details and a delete button.
 *
 * @param declaredShot The shot to display.
 * @param onDeleteShotClicked Callback triggered when the delete button is clicked.
 */
@Composable
fun ViewDeclaredShot(
    declaredShot: DeclaredShot,
    onDeleteShotClicked: (id: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = Padding.sixteen, end = Padding.sixteen, bottom = Padding.sixteen),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(Padding.sixteen))

        Row(verticalAlignment = Alignment.CenterVertically) {
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

        Row(verticalAlignment = Alignment.CenterVertically) {
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

/**
 * Displays a full-width delete button for removing the current declared shot.
 *
 * @param id The unique ID of the shot to delete.
 * @param onDeleteShotClicked Callback triggered when the delete button is clicked.
 */
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
                colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
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

/**
 * Preview the UI for creating a declared shot (CREATING state).
 */
@Preview(showBackground = true)
@Composable
fun CreateDeclaredShotPreview() {
    CreateEditDeclaredShotScreen(
        params = CreateEditDeclaredShotScreenParams(
            state = CreateEditDeclaredShotState(
                currentDeclaredShot = null,
                declaredShotState = DeclaredShotState.CREATING
            ),
            onToolbarMenuClicked = {},
            onDeleteShotClicked = {},
            onEditShotPencilClicked = {},
            onEditShotNameValueChanged = {},
            onEditShotDescriptionValueChanged = {},
            onEditShotCategoryValueChanged = {},
            onCreateShotNameValueChanged = {},
            onCreateShotDescriptionValueChanged = {},
            onCreateShotCategoryValueChanged = {},
            onEditOrCreateNewShot = {}
        )
    )
}

/**
 * Preview the UI for editing a declared shot (EDITING state).
 */
@Preview(showBackground = true)
@Composable
fun EditDeclaredShotPreview() {
    CreateEditDeclaredShotScreen(
        params = CreateEditDeclaredShotScreenParams(
            state = CreateEditDeclaredShotState(
                currentDeclaredShot = DeclaredShot(
                    id = 1,
                    shotCategory = "3-Pointer",
                    title = "Corner Shot",
                    description = "Shot from the right corner of the court.",
                    firebaseKey = "firebase_123"
                ),
                declaredShotState = DeclaredShotState.EDITING
            ),
            onToolbarMenuClicked = {},
            onDeleteShotClicked = {},
            onEditShotPencilClicked = {},
            onEditShotNameValueChanged = {},
            onEditShotDescriptionValueChanged = {},
            onEditShotCategoryValueChanged = {},
            onCreateShotNameValueChanged = {},
            onCreateShotDescriptionValueChanged = {},
            onCreateShotCategoryValueChanged = {},
            onEditOrCreateNewShot = {}
        )
    )
}

/**
 * Preview the UI for viewing a declared shot (VIEWING state).
 */
@Preview(showBackground = true)
@Composable
fun ViewDeclaredShotPreview() {
    CreateEditDeclaredShotScreen(
        params = CreateEditDeclaredShotScreenParams(
            state = CreateEditDeclaredShotState(
                currentDeclaredShot = DeclaredShot(
                    id = 2,
                    shotCategory = "Free Throw",
                    title = "Charity Stripe",
                    description = "Free throw shot from the free-throw line.",
                    firebaseKey = null
                ),
                declaredShotState = DeclaredShotState.VIEWING
            ),
            onToolbarMenuClicked = {},
            onDeleteShotClicked = {},
            onEditShotPencilClicked = {},
            onEditShotNameValueChanged = {},
            onEditShotDescriptionValueChanged = {},
            onEditShotCategoryValueChanged = {},
            onCreateShotNameValueChanged = {},
            onCreateShotDescriptionValueChanged = {},
            onCreateShotCategoryValueChanged = {},
            onEditOrCreateNewShot = {}
        )
    )
}

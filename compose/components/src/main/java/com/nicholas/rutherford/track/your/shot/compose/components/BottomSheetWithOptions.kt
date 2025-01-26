package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Basic [ModalBottomSheetLayout] that is reused across the app when displaying sheet with options
 *
 * @param sheetState [ModalBottomSheetState] which contains state of the sheet
 * @param sheetInfo [Sheet] optional param that define data for each sheet option
 * @param onSheetItemClicked [Unit] that is invoked when user clicks on a [Sheet] defined item
 * @param onCancelItemClicked [Unit] that is invoked when cancel sheet item is clicked
 * @param content [Composable] [Unit] that contains the User interface outside of the bottom sheet
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetWithOptions(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(Hidden),
    sheetInfo: Sheet? = null,
    onSheetItemClicked: (String) -> Unit = {},
    onCancelItemClicked: () -> Unit = {},
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            SheetContent(
                sheet = sheetInfo,
                onSheetItemClicked = onSheetItemClicked,
                onCancelItemClicked = onCancelItemClicked
            )
        }
    ) {
        content()
    }
}

@Composable
private fun SheetContent(
    sheet: Sheet?,
    onSheetItemClicked: (String) -> Unit,
    onCancelItemClicked: () -> Unit
) {
    sheet?.let {
        Text(
            text = it.title,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
            style = TextStyles.smallBold
        )
        LazyColumn {
            items(it.values) { value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 20.dp)
                        .clickable { onSheetItemClicked(value) }
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
    Text(
        text = stringResource(id = R.string.cancel),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp)
            .clickable { onCancelItemClicked() },
        style = TextStyles.body.copy(color = AppColors.Red)
    )
}

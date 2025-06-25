package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.R
import com.nicholas.rutherford.track.your.shot.data.shared.sheet.Sheet
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Basic Material 3 [ModalBottomSheet] reused across the app to display sheet with options
 *
 * @param sheetState [SheetState] which contains the state of the bottom sheet
 * @param sheetInfo [Sheet] optional param that defines the data for each sheet option
 * @param onSheetItemClicked Callback invoked when a user clicks on a [Sheet] item
 * @param onCancelItemClicked Callback invoked when the cancel button is clicked
 * @param content Composable content displayed behind the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithOptions(
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetInfo: Sheet? = null,
    onSheetItemClicked: (String, Int) -> Unit = { _, _ -> },
    onCancelItemClicked: () -> Unit = {},
    content: @Composable () -> Unit
) {
    content()

    if (sheetState.currentValue != SheetValue.Hidden && sheetInfo != null) {
        ModalBottomSheet(
            onDismissRequest = onCancelItemClicked,
            sheetState = sheetState,
            modifier = Modifier.fillMaxSize()
        ) {
            SheetContent(
                sheet = sheetInfo,
                onSheetItemClicked = onSheetItemClicked,
                onCancelItemClicked = onCancelItemClicked
            )
        }
    }
}

@Composable
private fun SheetContent(
    sheet: Sheet?,
    onSheetItemClicked: (String, Int) -> Unit = { _, _ -> },
    onCancelItemClicked: () -> Unit
) {
    sheet?.let {
        Text(
            text = it.title,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp),
            style = TextStyles.smallBold
        )
        LazyColumn {
            itemsIndexed(it.values) { index, value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 20.dp)
                        .clickable { onSheetItemClicked(value, index) }
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


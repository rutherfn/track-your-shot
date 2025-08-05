package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
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
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true // prevents sticky halfway behavior
    ),
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
            shape = RectangleShape,
            dragHandle = null,
            modifier = Modifier
                .windowInsetsPadding(BottomSheetDefaults.windowInsets)
                .wrapContentHeight()
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
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = it.title,
                style = TextStyles.bodyBold,
                modifier = Modifier
                    .padding(bottom = 12.dp)
            )

            HorizontalDivider()

            it.values.forEachIndexed { index, value ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSheetItemClicked(value, index) }
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = value,
                        style = TextStyles.body,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                if (index != it.values.lastIndex) {
                    HorizontalDivider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.cancel),
                style = TextStyles.body.copy(color = AppColors.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCancelItemClicked() }
                    .padding(start = 4.dp)
                    .padding(vertical = 14.dp)
            )
        }
    }
}

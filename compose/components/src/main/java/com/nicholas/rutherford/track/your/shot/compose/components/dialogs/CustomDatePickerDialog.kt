package com.nicholas.rutherford.track.your.shot.compose.components.dialogs

import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.toLocalDate
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Default [Dialog] with given params to build a custom date picker used for,
 * displaying in viewModel; whenever we want to select a new date
 *
 * @param datePickerInfo Contains all given data to build out the [CustomDatePickerDialog]
 */
@Composable
fun CustomDatePickerDialog(datePickerInfo: DatePickerInfo) {
    val selDate = remember { mutableStateOf(datePickerInfo.dateValue?.toLocalDate() ?: LocalDate.now()) }

    Dialog(
        onDismissRequest = { datePickerInfo.onDismissClicked?.invoke() },
        properties = DialogProperties()
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = StringsIds.selectDate).uppercase(Locale.ENGLISH),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColors.White
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, y")),
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppColors.White
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            AndroidView(
                modifier = Modifier.wrapContentSize(),
                factory = { context ->
                    CalendarView(context)
                },
                update = { view ->
                    view.date = selDate.value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    view.maxDate = System.currentTimeMillis()

                    view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                        val selectedLocalDate = LocalDate
                            .now()
                            .withMonth(month + 1)
                            .withYear(year)
                            .withDayOfMonth(dayOfMonth)

                        if (!selectedLocalDate.isAfter(LocalDate.now())) {
                            selDate.value = LocalDate
                                .now()
                                .withMonth(month + 1)
                                .withYear(year)
                                .withDayOfMonth(dayOfMonth)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = {
                        datePickerInfo.onDismissClicked?.invoke()
                    }
                ) {
                    Text(
                        text = stringResource(id = StringsIds.cancel),
                        color = AppColors.OrangeVariant
                    )
                }

                TextButton(
                    onClick = {
                        val datePattern = selDate.value.format(DateTimeFormatter.ofPattern("MMM d, y"))
                        datePickerInfo.onDateOkClicked.invoke(datePattern)
                    }
                ) {
                    Text(
                        text = stringResource(id = StringsIds.ok),
                        color = AppColors.Orange
                    )
                }
            }
        }
    }
}

/**
 * Preview of [CustomDatePickerDialog] with example date and callbacks.
 */
@Preview(showBackground = true)
@Composable
fun CustomDatePickerDialogPreview() {
    val datePickerInfo = DatePickerInfo(
        dateValue = "Aug 16, 2023",
        onDateOkClicked = {},
        onDismissClicked = {}
    )

    CustomDatePickerDialog(datePickerInfo = datePickerInfo)
}

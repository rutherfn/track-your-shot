package com.nicholas.rutherford.track.your.shot.data.shared.datepicker

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Data class representing the configuration and callbacks for a date picker UI component.
 *
 * @property onDateOkClicked Callback invoked when the user confirms a selected date. Provides the selected date as a String.
 * @property onDismissClicked Optional callback invoked when the date picker is dismissed without selecting a date.
 * @property dateValue Optional preselected date value to display in the date picker.
 */
data class DatePickerInfo(
    val onDateOkClicked: ((value: String) -> Unit),
    val onDismissClicked: (() -> Unit)? = null,
    val dateValue: String? = null
)

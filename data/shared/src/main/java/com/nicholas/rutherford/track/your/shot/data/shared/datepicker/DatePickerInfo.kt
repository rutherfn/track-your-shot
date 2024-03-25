package com.nicholas.rutherford.track.your.shot.data.shared.datepicker

data class DatePickerInfo(
    val onDateOkClicked: ((value: String) -> Unit),
    val onDismissClicked: (() -> Unit)? = null,
    val dateValue: String? = null
)

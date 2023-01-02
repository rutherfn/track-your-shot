package com.nicholas.rutherford.track.my.shot.feature.create.account

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert

data class CreateAccountState(
    val username: String?,
    val email: String?,
    val password: String?,
    val alert: Alert?
)

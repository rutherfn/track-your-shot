package com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

data class AccountInfoState(
    val shouldEditAccountInfoDetails: Boolean = false,
    val toolbarTitleId: Int = StringsIds.accountInfo,
    val toolbarSecondaryImageVector: ImageVector = Icons.Filled.Edit,
    val email: String = "",
    val newEmail: String = "",
    val confirmNewEmail: String = "",
    val username: String = "",
    val newUsername: String = "",
    val confirmNewUsername: String = ""
)

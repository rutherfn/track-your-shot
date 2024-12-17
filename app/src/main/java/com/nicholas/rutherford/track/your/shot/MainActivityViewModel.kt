package com.nicholas.rutherford.track.your.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager

class MainActivityViewModel(
    private val accountManager: AccountManager
) : ViewModel() {

    fun logout(titleId: Int) {
        if (titleId == StringsIds.logout) {
            accountManager.logout()
        }
    }
}

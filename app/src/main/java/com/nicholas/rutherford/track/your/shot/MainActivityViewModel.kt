package com.nicholas.rutherford.track.your.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.app.center.AppCenter
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountManager

class MainActivityViewModel(
    private val appCenter: AppCenter,
    private val accountManager: AccountManager
) : ViewModel() {

    fun initAppCenter() = appCenter.start()

    fun logout(titleId: Int) {
        if (titleId == StringsIds.logout) {
            accountManager.logout()
        }
    }
}

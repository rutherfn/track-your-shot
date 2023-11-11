package com.nicholas.rutherford.track.your.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.app.center.AppCenter
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.account.AccountAuthManager

class MainActivityViewModel(
    private val appCenter: AppCenter,
    private val accountAuthManager: AccountAuthManager
) : ViewModel() {

    fun initAppCenter() = appCenter.start()

    fun logout(titleId: Int) {
        if (titleId == StringsIds.logout) {
            accountAuthManager.logout()
        }
    }
}

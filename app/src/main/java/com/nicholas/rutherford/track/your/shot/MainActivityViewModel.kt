package com.nicholas.rutherford.track.your.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.app.center.AppCenter

class MainActivityViewModel(private val appCenter: AppCenter) : ViewModel() {

    fun initAppCenter() = appCenter.start()
}

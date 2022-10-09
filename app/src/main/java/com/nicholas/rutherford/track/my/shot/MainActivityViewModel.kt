package com.nicholas.rutherford.track.my.shot

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.app.center.AppCenter

class MainActivityViewModel(private val appCenter: AppCenter) : ViewModel() {

    fun initAppCenter() = appCenter.start()
}
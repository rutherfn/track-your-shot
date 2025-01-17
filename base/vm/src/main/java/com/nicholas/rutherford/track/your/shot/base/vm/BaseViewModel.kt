package com.nicholas.rutherford.track.your.shot.base.vm

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    open fun onNavigatedTo() {
    }
}

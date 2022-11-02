package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val navigator: Navigator) : ViewModel() {

    init {
        testNavigator()
    }

    fun testNavigator() {
        viewModelScope.launch {
            delay(4000)
            navigator.navigate(NavigationActions.SplashScreen.navigateToHome())
        }
    }
}

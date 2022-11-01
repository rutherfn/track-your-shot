package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator

class SplashViewModel(private val navigator: Navigator) : ViewModel() {

    fun testNavigator() {
        navigator.navigate(NavigationActions.SplashScreen.navigateToHome())
    }
}

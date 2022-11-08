package com.nicholas.rutherford.track.my.shot.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholas.rutherford.track.my.shot.navigation.NavigationAction
import com.nicholas.rutherford.track.my.shot.navigation.NavigationActions
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val SPLASH_DELAY_IN_MILLIS = 4000L
const val SPLASH_IMAGE_SCALE = 1f

class SplashViewModel(private val navigator: Navigator) : ViewModel() {

    internal var navigationDestination: NavigationAction? = null

    internal val initializeSplashState = SplashState(
        backgroundColor = Colors.splashBackgroundColor,
        imageScale = SPLASH_IMAGE_SCALE,
        imageDrawableId = DrawablesIds.splash
    )

    private val _splashState = MutableStateFlow(value = initializeSplashState)
    val splashState = _splashState.asStateFlow()

    init {
        delayAndNavigateToHome()
    }

    fun delayAndNavigateToHome() {
        viewModelScope.launch {
            delay(timeMillis = SPLASH_DELAY_IN_MILLIS)

            navigationDestination = NavigationActions.SplashScreen.navigateToHome()
            navigator.navigate(NavigationActions.SplashScreen.navigateToHome())
        }
    }
}

package com.nicholas.rutherford.track.my.shot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.nicholas.rutherford.track.my.shot.feature.home.HomeScreen
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashScreen
import com.nicholas.rutherford.track.my.shot.navigation.NavigationComponent
import com.nicholas.rutherford.track.my.shot.navigation.Navigator
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initAppCenter()
        setContent {
            val navigator: Navigator = get()
            NavigationComponent(
                navHostController = rememberNavController(),
                navigator = navigator,
                splashContent = { SplashScreen(viewModel = getViewModel()) }, homeContent = { HomeScreen(viewModel = getViewModel()) }
            )
        }
    }
}

package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.your.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashViewModel

data class ViewModels(
    val mainActivityViewModel: MainActivityViewModel,
    val splashViewModel: SplashViewModel,
    val loginViewModel: LoginViewModel,
    val playersListViewModel: PlayersListViewModel,
    val forgotPasswordViewModel: ForgotPasswordViewModel,
    val createAccountViewModel: CreateAccountViewModel,
    val authenticationViewModel: AuthenticationViewModel
)

package com.nicholas.rutherford.track.my.shot

import com.nicholas.rutherford.track.my.shot.feature.create.account.authentication.AuthenticationViewModel
import com.nicholas.rutherford.track.my.shot.feature.create.account.createaccount.CreateAccountViewModel
import com.nicholas.rutherford.track.my.shot.feature.forgot.password.ForgotPasswordViewModel
import com.nicholas.rutherford.track.my.shot.feature.home.HomeViewModel
import com.nicholas.rutherford.track.my.shot.feature.login.LoginViewModel
import com.nicholas.rutherford.track.my.shot.feature.splash.SplashViewModel

data class ViewModels(
    val splashViewModel: SplashViewModel,
    val loginViewModel: LoginViewModel,
    val homeViewModel: HomeViewModel,
    val forgotPasswordViewModel: ForgotPasswordViewModel,
    val createAccountViewModel: CreateAccountViewModel,
    val authenticationViewModel: AuthenticationViewModel
)

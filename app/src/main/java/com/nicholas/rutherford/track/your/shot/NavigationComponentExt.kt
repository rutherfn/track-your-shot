package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.compose.components.AppBar2
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations

object NavigationComponentExt {

    fun findViewModelByDestination(destination: String, viewModels: ViewModels): BaseViewModel? {
        return when {
            destination.contains(NavigationDestinations.SPLASH_SCREEN) -> viewModels.splashViewModel
            destination.contains(NavigationDestinations.CREATE_ACCOUNT_SCREEN) -> viewModels.createAccountViewModel
            destination.contains(NavigationDestinations.LOGIN_SCREEN) -> viewModels.loginViewModel
            destination.contains(NavigationDestinations.FORGOT_PASSWORD_SCREEN) -> viewModels.forgotPasswordViewModel
            destination.contains(NavigationDestinations.REPORTS_LIST_SCREEN) -> viewModels.reportListViewModel
            destination.contains(NavigationDestinations.CREATE_REPORT_SCREEN) -> viewModels.createReportViewModel
            destination.contains(NavigationDestinations.PLAYERS_LIST_SCREEN) -> viewModels.playersListViewModel
            destination.contains(NavigationDestinations.SELECT_SHOT_SCREEN) -> viewModels.selectShotViewModel
            destination.contains(NavigationDestinations.SHOTS_LIST_SCREEN) -> viewModels.shotsListViewModel
            destination.contains(NavigationDestinations.DECLARED_SHOTS_LIST_SCREEN) -> viewModels.declaredShotsListViewModel
            destination.contains(NavigationDestinations.CREATE_EDIT_DECLARED_SHOTS_SCREEN) -> viewModels.createEditDeclaredShotsViewModel
            destination.contains(NavigationDestinations.SETTINGS_SCREEN) -> viewModels.settingsViewModel
            destination.contains(NavigationDestinations.PERMISSION_EDUCATION_SCREEN) -> viewModels.permissionEducationViewModel
            destination.contains(NavigationDestinations.ENABLED_PERMISSIONS_SCREEN) -> viewModels.enabledPermissionsViewModel
            destination.contains(NavigationDestinations.ONBOARDING_EDUCATION_SCREEN) -> viewModels.onboardingEducationViewModel
            destination.contains(NavigationDestinations.TERMS_CONDITIONS_SCREEN) -> viewModels.termsConditionsViewModel
            else -> null
        }
    }

    fun buildModalDrawerGesturesEnabled(viewModel: BaseViewModel, viewModels: ViewModels): Boolean = viewModel == viewModels.playersListViewModel || viewModel == viewModels.shotsListViewModel || viewModel == viewModels.reportListViewModel || viewModel == viewModels.settingsViewModel

    fun buildAppBarBasedOnScreen(
        viewModel: BaseViewModel,
        viewModels: ViewModels,
        viewModelParams: ViewModelsParams,
        screenAppBars: ScreenAppBars
    ): AppBar2? {
        return when (viewModel) {
            viewModels.splashViewModel -> null
            viewModels.createAccountViewModel -> screenAppBars.buildCreateAccountAppBar(createAccountViewModel = viewModels.createAccountViewModel)
            viewModels.forgotPasswordViewModel -> screenAppBars.buildForgotPasswordAppBar(forgotPasswordViewModel = viewModels.forgotPasswordViewModel)
            viewModels.playersListViewModel -> screenAppBars.buildPlayersListAppBar(playersListViewModel = viewModels.playersListViewModel)
            viewModels.shotsListViewModel -> screenAppBars.buildShotsListAppBar(params = viewModelParams.shotListParams)
            viewModels.reportListViewModel -> screenAppBars.buildReportListAppBar(params = viewModelParams.reportListParams)
            viewModels.settingsViewModel -> screenAppBars.buildSettingsAppBar(params = viewModelParams.settingsParams)
            viewModels.permissionEducationViewModel -> screenAppBars.buildPermissionEducationAppBar(viewModel = viewModels.permissionEducationViewModel)
            viewModels.enabledPermissionsViewModel -> screenAppBars.buildEnabledPermissionAppBar(params = viewModelParams.enabledPermissionsParams)
            viewModels.onboardingEducationViewModel -> screenAppBars.buildOnboardingEducationAppBar(viewModel = viewModels.onboardingEducationViewModel)
            viewModels.termsConditionsViewModel -> screenAppBars.buildTermsAndConditionsAppBar()
            viewModels.declaredShotsListViewModel -> screenAppBars.buildDeclaredShotListAppBar(params = viewModelParams.declaredShotsListScreenParams)
            viewModels.createEditDeclaredShotsViewModel -> screenAppBars.buildCreateEditDeclaredShotAppBar(params = viewModelParams.createEditDeclaredShotParams)

            else -> screenAppBars.buildDefaultAppBar()
        }
    }
}
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
            destination.contains(NavigationDestinations.AUTHENTICATION_SCREEN) -> viewModels.authenticationViewModel
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
            destination.contains(NavigationDestinations.ACCOUNT_INFO_SCREEN) -> viewModels.accountInfoViewModel
            else -> null
        }
    }

    fun buildModalDrawerGesturesEnabled(viewModel: BaseViewModel, viewModels: ViewModels): Boolean = viewModel == viewModels.playersListViewModel || viewModel == viewModels.shotsListViewModel || viewModel == viewModels.reportListViewModel || viewModel == viewModels.settingsViewModel

    fun buildAppBarBasedOnScreen(
        viewModel: BaseViewModel,
        viewModels: ViewModels,
        viewModelParams: ViewModelsParams,
        appBarFactory: AppBarFactory
    ): AppBar2? {
        return when (viewModel) {
            viewModels.splashViewModel -> null
            viewModels.loginViewModel -> appBarFactory.createLoginAppBar()
            viewModels.createAccountViewModel -> appBarFactory.createCreateAccountAppBar(viewModel = viewModels.createAccountViewModel)
            viewModels.forgotPasswordViewModel -> appBarFactory.createForgotPasswordAppBar(viewModel = viewModels.forgotPasswordViewModel)
            viewModels.authenticationViewModel -> appBarFactory.createAuthenticationAppBar(viewModel = viewModels.authenticationViewModel)
            viewModels.playersListViewModel -> appBarFactory.createPlayersListAppBar(viewModel = viewModels.playersListViewModel)
            viewModels.shotsListViewModel -> appBarFactory.createShotsListAppBar(params = viewModelParams.shotListParams)
            viewModels.reportListViewModel -> appBarFactory.createReportListAppBar(params = viewModelParams.reportListParams)
            viewModels.settingsViewModel -> appBarFactory.createSettingsAppBar(params = viewModelParams.settingsParams)
            viewModels.permissionEducationViewModel -> appBarFactory.createPermissionEducationAppBar(viewModel = viewModels.permissionEducationViewModel)
            viewModels.enabledPermissionsViewModel -> appBarFactory.createEnabledPermissionsAppBar(params = viewModelParams.enabledPermissionsParams)
            viewModels.onboardingEducationViewModel -> appBarFactory.createOnboardingEducationAppBar(viewModel = viewModels.onboardingEducationViewModel)
            viewModels.termsConditionsViewModel -> appBarFactory.createTermsAndConditionsAppBar()
            viewModels.declaredShotsListViewModel -> appBarFactory.createDeclaredShotsListAppBar(params = viewModelParams.declaredShotsListScreenParams)
            viewModels.createEditDeclaredShotsViewModel -> appBarFactory.createCreateEditDeclaredShotAppBar(params = viewModelParams.createEditDeclaredShotParams)
            viewModels.accountInfoViewModel -> appBarFactory.createAccountInfoAppBar(viewModel = viewModels.accountInfoViewModel)

            else -> appBarFactory.createDefaultAppBar()
        }
    }
}
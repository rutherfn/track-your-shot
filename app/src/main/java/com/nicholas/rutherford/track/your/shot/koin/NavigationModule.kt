package com.nicholas.rutherford.track.your.shot.koin

import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationNavigation
import com.nicholas.rutherford.track.your.shot.feature.create.account.authentication.AuthenticationNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountNavigation
import com.nicholas.rutherford.track.your.shot.feature.create.account.createaccount.CreateAccountNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordNavigation
import com.nicholas.rutherford.track.your.shot.feature.forgot.password.ForgotPasswordNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.login.LoginNavigation
import com.nicholas.rutherford.track.your.shot.feature.login.LoginNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.createeditplayer.CreateEditPlayerNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.playerlist.PlayersListNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.LogShotNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotNavigation
import com.nicholas.rutherford.track.your.shot.feature.players.shots.selectshot.SelectShotNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportNavigation
import com.nicholas.rutherford.track.your.shot.feature.reports.createreport.CreateReportNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListNavigation
import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.debugtoggle.DebugToggleNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.onboardingeducation.OnboardingEducationNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation.PermissionEducationNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsNavigation
import com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions.TermsConditionsNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListNavigation
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListNavigationImpl
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigation
import com.nicholas.rutherford.track.your.shot.feature.splash.SplashNavigationImpl
import org.koin.dsl.module

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Koin module for providing navigation dependencies throughout the app.
 *
 * Each navigation interface represents a specific feature or screen flow in the app.
 * This module binds the interfaces to their corresponding implementations.
 */
object NavigationModule {

    /** Koin module definitions. */
    val modules = module {

        /** Navigation for the Splash screen. */
        single<SplashNavigation> {
            SplashNavigationImpl(navigator = get())
        }

        /** Navigation for the Login screen. */
        single<LoginNavigation> {
            LoginNavigationImpl(navigator = get())
        }

        /** Navigation for the Forgot Password screen. */
        single<ForgotPasswordNavigation> {
            ForgotPasswordNavigationImpl(navigator = get())
        }

        /** Navigation for the Create Account screen. */
        single<CreateAccountNavigation> {
            CreateAccountNavigationImpl(navigator = get())
        }

        /** Navigation for the Authentication screen. */
        single<AuthenticationNavigation> {
            AuthenticationNavigationImpl(navigator = get())
        }

        /** Navigation for the Players List screen. */
        single<PlayersListNavigation> {
            PlayersListNavigationImpl(navigator = get())
        }

        /** Navigation for Create/Edit Player screen. */
        single<CreateEditPlayerNavigation> {
            CreateEditPlayerNavigationImpl(navigator = get())
        }

        /** Navigation for Select Shot screen. */
        single<SelectShotNavigation> {
            SelectShotNavigationImpl(navigator = get())
        }

        /** Navigation for Log Shot screen. */
        single<LogShotNavigation> {
            LogShotNavigationImpl(navigator = get())
        }

        /** Navigation for Settings screen. */
        single<SettingsNavigation> {
            SettingsNavigationImpl(navigator = get())
        }

        /** Navigation for Permission Education screen. */
        single<PermissionEducationNavigation> {
            PermissionEducationNavigationImpl(navigator = get())
        }

        /** Navigation for Terms and Conditions screen. */
        single<TermsConditionsNavigation> {
            TermsConditionsNavigationImpl(navigator = get())
        }

        /** Navigation for Enabled Permissions screen. */
        single<EnabledPermissionsNavigation> {
            EnabledPermissionsNavigationImpl(navigator = get())
        }

        /** Navigation for Debug Toggle screen. */
        single<DebugToggleNavigation> {
            DebugToggleNavigationImpl(navigator = get())
        }

        /** Navigation for Onboarding Education screen. */
        single<OnboardingEducationNavigation> {
            OnboardingEducationNavigationImpl(navigator = get())
        }

        /** Navigation for Account Info screen. */
        single<AccountInfoNavigation> {
            AccountInfoNavigationImpl(navigator = get())
        }

        /** Navigation for Report List screen. */
        single<ReportListNavigation> {
            ReportListNavigationImpl(navigator = get())
        }

        /** Navigation for Create Report screen. */
        single<CreateReportNavigation> {
            CreateReportNavigationImpl(navigator = get())
        }

        /** Navigation for Shots List screen. */
        single<ShotsListNavigation> {
            ShotsListNavigationImpl(navigator = get())
        }

        /** Navigation for Declared Shots List screen. */
        single<DeclaredShotsListNavigation> {
            DeclaredShotsListNavigationImpl(navigator = get())
        }

        /** Navigation for Create/Edit Declared Shot screen. */
        single<CreateEditDeclaredShotNavigation> {
            CreateEditDeclaredShotNavigationImpl(navigator = get())
        }
    }
}

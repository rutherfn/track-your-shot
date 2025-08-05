package com.nicholas.rutherford.track.your.shot

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nicholas.rutherford.track.your.shot.NavigationComponentExt.buildModalDrawerGesturesEnabled
import com.nicholas.rutherford.track.your.shot.NavigationComponentExt.findViewModelByDestination
import com.nicholas.rutherford.track.your.shot.compose.components.ConditionalTopAppBar
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.AlertDialog
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.CustomDatePickerDialog
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.ProgressDialog
import com.nicholas.rutherford.track.your.shot.compose.components.dialogs.ShotInputDialog
import com.nicholas.rutherford.track.your.shot.data.shared.InputInfo
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.data.shared.datepicker.DatePickerInfo
import com.nicholas.rutherford.track.your.shot.data.shared.progress.Progress
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.LogoutAction
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.navigation.PlayersListAction
import com.nicholas.rutherford.track.your.shot.navigation.ReportingAction
import com.nicholas.rutherford.track.your.shot.navigation.SettingsAction
import com.nicholas.rutherford.track.your.shot.navigation.ShotsAction
import com.nicholas.rutherford.track.your.shot.navigation.asLifecycleAwareState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationComponent(
    activity: MainActivity,
    navHostController: NavHostController,
    navigator: Navigator,
    viewModels: ViewModels
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    val alertState by navigator.alertActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val appSettingsState by navigator.appSettingsActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val datePickerState by navigator.datePickerActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val emailState by navigator.emailActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val emailDevState by navigator.emailDevActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val finishState by navigator.finishActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val inputInfoState by navigator.inputInfoActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val navigatorState by navigator.navActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val popRouteState by navigator.popRouteActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val progressState by navigator.progressActions.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val navigationDrawerState by navigator.navigationDrawerAction.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )
    val urlState by navigator.urlAction.asLifecycleAwareState(
        lifecycleOwner = lifecycleOwner,
        initialState = null
    )

    var alert: Alert? by remember { mutableStateOf(value = null) }
    var datePicker: DatePickerInfo? by remember { mutableStateOf(value = null) }
    var inputInfo: InputInfo? by remember { mutableStateOf(value = null) }
    var progress: Progress? by remember { mutableStateOf(value = null) }
    var modalDrawerGesturesEnabled: Boolean by remember { mutableStateOf(value = false) }
    val appBar = AppNavigationGraph.currentAppBar

    val mainActivityViewModel = viewModels.mainActivityViewModel

    val isConnectedToInternet = mainActivityViewModel.isConnected.collectAsState().value

    LaunchedEffect(alertState) {
        alertState?.let { newAlert ->
            alert = newAlert
        }
    }
    LaunchedEffect(datePickerState) {
        datePickerState?.let { newDatePicker ->
            datePicker = newDatePicker
        }
    }
    LaunchedEffect(inputInfoState) {
        inputInfoState?.let { newInputInfo ->
            inputInfo = newInputInfo
        }
    }
    LaunchedEffect(appSettingsState) {
        appSettingsState?.let { shouldOpenAppSettings ->
            if (shouldOpenAppSettings) {
                val intent = Intent()

                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri

                activity.startActivity(intent)

                navigator.appSettings(appSettingsAction = null)
            }
        }
    }
    LaunchedEffect(emailState) {
        emailState?.let { shouldAttemptToOpenEmail ->
            if (shouldAttemptToOpenEmail) {
                try {
                    val intent = Intent(Intent.ACTION_MAIN)

                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                    activity.startActivity(intent)
                    navigator.emailAction(emailAction = null)
                } catch (ex: ActivityNotFoundException) {
                    ex.printStackTrace()
                }
            }
        }
    }
    LaunchedEffect(emailDevState) {
        emailDevState?.let { devEmail ->
            try {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "${Constants.MAIL_TO}$devEmail".toUri()
                }

                activity.startActivity(intent)
                navigator.emailDevAction(emailDevAction = null)
            } catch (ex: ActivityNotFoundException) {
                ex.printStackTrace()
            }
        }
    }

    LaunchedEffect(navigatorState) {
        navigatorState?.let { state ->
            navHostController.navigate(state.destination, state.navOptions)
            val viewModel = findViewModelByDestination(destination = state.destination, viewModels = viewModels)

            if (viewModel != null) {
                modalDrawerGesturesEnabled = buildModalDrawerGesturesEnabled(viewModel = viewModel, viewModels = viewModels)
            }
        }
    }

    LaunchedEffect(finishState) {
        finishState?.let { shouldFinish ->
            if (shouldFinish) {
                activity.finish()
                navigator.finish(finishAction = null)
            }
        }
    }

    LaunchedEffect(popRouteState) {
        popRouteState?.let { route ->
            if (route == Constants.POP_DEFAULT_ACTION) {
                navHostController.popBackStack()
            } else {
                navHostController.popBackStack(route = route, inclusive = false)
            }
            navigator.pop(popRouteAction = null)
        }
    }

    LaunchedEffect(progressState) {
        progressState?.let { newProgress ->
            progress = newProgress
        } ?: run {
            progress = null
        }
    }

    LaunchedEffect(navigationDrawerState) {
        navigationDrawerState?.let { shouldOpenNavigationDrawer ->
            scope.launch {
                if (shouldOpenNavigationDrawer) {
                    drawerState.open()
                } else {
                    drawerState.close()
                }
                navigator.showNavigationDrawer(navigationDrawerAction = null)
            }
        }
    }

    LaunchedEffect(urlState) {
        urlState?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            activity.startActivity(intent)
            navigator.url(url = null)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                actions = listOf(
                    PlayersListAction,
                    ShotsAction,
                    ReportingAction,
                    SettingsAction,
                    LogoutAction
                ),
                onDestinationClicked = { route, navOptions, titleId ->
                    scope.launch { drawerState.close() }
                    if (route.isEmpty()) {
                        mainActivityViewModel.logout(titleId = titleId)
                    } else {
                        val currentRoute = navHostController.currentDestination?.route ?: ""
                        if (route != currentRoute) {
                            navHostController.navigate(route, navOptions)
                            val viewModel = findViewModelByDestination(destination = route, viewModels = viewModels)

                            if (viewModel != null) {
                                modalDrawerGesturesEnabled = buildModalDrawerGesturesEnabled(viewModel = viewModel, viewModels = viewModels)
                            }
                        }
                    }
                }
            )
        },
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    appBar?.let { bar ->
                        if (bar.shouldShow) {
                            ConditionalTopAppBar(appBar = bar)
                        }
                    } ?: run {
                        TopAppBar(
                            title = {},
                            navigationIcon = {},
                            actions = {},
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = Color.Unspecified,
                                navigationIconContentColor = Color.Unspecified,
                                actionIconContentColor = Color.Unspecified
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent),
                            scrollBehavior = null
                        )
                    }
                }
            ) { paddingValues ->
                NavHost(
                    navController = navHostController,
                    modifier = Modifier
                        .padding(paddingValues),
                    startDestination = NavigationDestinations.SPLASH_SCREEN
                ) {
                    AppNavigationRegistry.registerAll(navGraphBuilder = this, isConnectedToInternet = isConnectedToInternet)
                }
            }

            inputInfo?.let { info ->
                ShotInputDialog(
                    inputInfo = InputInfo(
                        titleResId = info.titleResId,
                        confirmButtonResId = info.confirmButtonResId,
                        dismissButtonResId = info.dismissButtonResId,
                        placeholderResId = info.placeholderResId,
                        startingInputAmount = info.startingInputAmount,
                        onConfirmButtonClicked = { value ->
                            navigator.inputInfo(inputInfoAction = null)
                            info.onConfirmButtonClicked.invoke(value)
                            inputInfo = null
                        },
                        onDismissButtonClicked = {
                            navigator.inputInfo(inputInfoAction = null)
                            info.onDismissButtonClicked?.invoke()
                            inputInfo = null
                        }
                    )
                )
            }

            datePicker?.let { newDatePicker ->
                TrackYourShotTheme {
                    CustomDatePickerDialog(
                        datePickerInfo = DatePickerInfo(
                            onDateOkClicked = { value ->
                                navigator.datePicker(datePickerAction = null)
                                datePicker = null
                                newDatePicker.onDateOkClicked.invoke(value)
                            },
                            onDismissClicked = {
                                navigator.datePicker(datePickerAction = null)
                                datePicker = null
                                newDatePicker.onDismissClicked?.invoke()
                            },
                            dateValue = newDatePicker.dateValue
                        )
                    )
                }
            }

            alert?.let { newAlert ->
                AlertDialog(
                    onDismissClicked = {
                        navigator.alert(alertAction = null)
                        alert = null
                        newAlert.onDismissClicked?.invoke()
                    },
                    title = newAlert.title,
                    confirmButton = newAlert.confirmButton?.let { confirmButton ->
                        AlertConfirmAndDismissButton(
                            onButtonClicked = {
                                navigator.alert(alertAction = null)
                                alert = null
                                confirmButton.onButtonClicked?.invoke()
                            },
                            buttonText = confirmButton.buttonText
                        )
                    } ?: run { null },
                    dismissButton = newAlert.dismissButton?.let { dismissButton ->
                        AlertConfirmAndDismissButton(
                            onButtonClicked = {
                                navigator.alert(alertAction = null)
                                alert = null
                                dismissButton.onButtonClicked?.invoke()
                            },
                            buttonText = dismissButton.buttonText
                        )
                    } ?: run { null },
                    description = newAlert.description
                )
            }

            progress?.let { newProgress ->
                ProgressDialog(
                    onDismissClicked = {
                        if (newProgress.shouldBeAbleToBeDismissed) {
                            navigator.progress(progressAction = null)
                            progress = null
                        }
                        newProgress.onDismissClicked?.invoke()
                    },
                    title = newProgress.title
                )
            }
        },
        gesturesEnabled = modalDrawerGesturesEnabled
    )
}

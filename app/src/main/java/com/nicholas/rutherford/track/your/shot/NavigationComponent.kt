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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.nicholas.rutherford.track.your.shot.data.shared.snackbar.SnackBarInfo
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.navigation.NavigationDestinations
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import com.nicholas.rutherford.track.your.shot.navigation.asLifecycleAwareState
import kotlinx.coroutines.launch

/**
 * Root navigation component for the app.
 *
 * Handles:
 * - Navigation between screens via [NavHostController].
 * - Modal drawer gestures and drawer content.
 * - Lifecycle-aware dialogs (alerts, progress dialogs, input dialogs, date pickers).
 * - System actions like email, app settings, URL opening, and finishing the activity.
 *
 * @param activity The hosting [MainActivity], used for launching intents and finishing the activity.
 * @param navHostController The [NavHostController] used to control navigation between screens.
 * @param navigator The [Navigator] containing flows for navigation, alerts, progress, dialogs, and system actions.
 * @param viewModels Container of all ViewModels used in the app, used to map destinations to their state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationComponent(
    activity: MainActivity,
    navHostController: NavHostController,
    navigator: Navigator,
    viewModels: ViewModels
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    // Lifecycle-aware state for alerts, dialogs, and navigation
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
    val snackBarState by navigator.snackBarActions.asLifecycleAwareState(
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
    var snackBarInfo: SnackBarInfo? by remember { mutableStateOf(value = null) }
    var modalDrawerGesturesEnabled: Boolean by remember { mutableStateOf(value = false) }
    val appBar = AppNavigationGraph.currentAppBar

    val mainActivityViewModel = viewModels.mainActivityViewModel

    val isConnectedToInternet = mainActivityViewModel.isConnected.collectAsState().value

    // Update UI state based on lifecycle-aware states
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
    // Open system app settings if requested
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
    // Open default email app if requested
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
    // Open developer email client if requested
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
    // Navigate to new destination
    LaunchedEffect(navigatorState) {
        navigatorState?.let { state ->
            navHostController.navigate(state.destination, state.navOptions)
            val viewModel = findViewModelByDestination(destination = state.destination, viewModels = viewModels)

            if (viewModel != null) {
                modalDrawerGesturesEnabled = buildModalDrawerGesturesEnabled(viewModel = viewModel, viewModels = viewModels)
            }
        }
    }
    // Finish activity if requested
    LaunchedEffect(finishState) {
        finishState?.let { shouldFinish ->
            if (shouldFinish) {
                activity.finish()
                navigator.finish(finishAction = null)
            }
        }
    }
    // Pop a route from the backstack
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
    // Show or hide progress dialog
    LaunchedEffect(progressState) {
        progressState?.let { newProgress ->
            progress = newProgress
        } ?: run {
            progress = null
        }
    }
    // Show or hide snackBar
    LaunchedEffect(snackBarState) {
        snackBarState?.let { state ->
            snackBarInfo = state
            navigator.snackBar(snackBarInfo = null)
        } ?: run {
            snackBarInfo = null
            navigator.snackBar(snackBarInfo = null)
        }
    }
    // Open or close navigation drawer
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
    // Open URL via intent
    LaunchedEffect(urlState) {
        urlState?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            activity.startActivity(intent)
            navigator.url(url = null)
        }
    }
    // Main UI layout with modal navigation drawer and scaffold
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                actions = mainActivityViewModel.buildDrawerActions(),
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
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
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

            // Render dialogs if state is present
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

            snackBarInfo?.let { newSnackBarInfo ->
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = newSnackBarInfo.message,
                        actionLabel = newSnackBarInfo.actionLabel,
                        withDismissAction = newSnackBarInfo.withDismissAction,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        },
        gesturesEnabled = modalDrawerGesturesEnabled
    )
}

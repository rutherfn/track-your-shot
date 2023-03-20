package com.nicholas.rutherford.track.my.shot.navigation

import com.nicholas.rutherford.track.my.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.my.shot.data.shared.dialogtextfield.DialogTextField
import com.nicholas.rutherford.track.my.shot.data.shared.progress.Progress
import kotlinx.coroutines.flow.StateFlow

interface Navigator {
    val alertActions: StateFlow<Alert?>
    val dialogWithTextFieldActions: StateFlow<DialogTextField?>
    val emailActions: StateFlow<Boolean?>
    val finishActions: StateFlow<Boolean?>
    val navActions: StateFlow<NavigationAction?>
    val popRouteActions: StateFlow<String?>
    val progressActions: StateFlow<Progress?>

    fun alert(alertAction: Alert?)
    fun dialogWithTextField(dialogWithTextFieldAction:DialogTextField?)
    fun emailAction(emailAction: Boolean?)
    fun finish(finishAction: Boolean?)
    fun navigate(navigationAction: NavigationAction?)
    fun pop(popRouteAction: String?)
    fun progress(progressAction: Progress?)
}

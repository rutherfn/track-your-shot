package com.nicholas.rutherford.track.your.shot

import com.nicholas.rutherford.track.your.shot.feature.reports.reportlist.ReportListParams
import com.nicholas.rutherford.track.your.shot.feature.settings.SettingsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.accountinfo.AccountInfoParams
import com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions.EnabledPermissionsParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.createeditdeclaredshot.CreateEditDeclaredShotScreenParams
import com.nicholas.rutherford.track.your.shot.feature.settings.managedeclaredshots.declaredshotslist.DeclaredShotsListScreenParams
import com.nicholas.rutherford.track.your.shot.feature.shots.ShotsListScreenParams

data class ViewModelsParams(
    val reportListParams: ReportListParams,
    val shotListParams: ShotsListScreenParams,
    val enabledPermissionsParams: EnabledPermissionsParams,
    val declaredShotsListScreenParams: DeclaredShotsListScreenParams,
    val createEditDeclaredShotParams: CreateEditDeclaredShotScreenParams,
    val settingsParams: SettingsParams
)
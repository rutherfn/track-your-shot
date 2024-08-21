package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds

data class EnabledPermissionsState(
    val mediaOrExternalStorageStringId: Int = StringsIds.empty,
    val cameraPermissionStringId: Int = StringsIds.empty
)
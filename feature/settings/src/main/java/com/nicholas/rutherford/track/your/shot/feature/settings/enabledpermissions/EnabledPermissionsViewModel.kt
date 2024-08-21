package com.nicholas.rutherford.track.your.shot.feature.settings.enabledpermissions

import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.isTiramisuOrAbove
import com.nicholas.rutherford.track.your.shot.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EnabledPermissionsViewModel(
    private val buildType: BuildType,
    private val navigator: Navigator
) : ViewModel() {

    init {
       updateState()
    }

    internal val enabledPermissionMutableStateFlow = MutableStateFlow(value = EnabledPermissionsState())
    val enabledPermissionStateFlow = enabledPermissionMutableStateFlow.asStateFlow()

    fun updateState() {
        val mediaOrExternalStorageResId = if (isTiramisuOrAbove(sdk = buildType.sdk)) {
            StringsIds.readMediaImagesPermission
        } else {
            StringsIds.readExternalStoragePermission
        }

        enabledPermissionMutableStateFlow.update { state ->
            state.copy(
              //  mediaOrExternalStorageStringId = mediaOrExternalStorageResId,
             //   cameraPermissionStringId = StringsIds.cameraPermission
            )
        }
    }

    fun onBackButtonClicked() = navigator.pop(popRouteAction = Constants.POP_DEFAULT_ACTION)
}
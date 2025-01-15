package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.DrawablesIds
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.helper.extensions.isTiramisuOrAbove
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionEducationViewModel(
    private val navigation: PermissionEducationNavigation,
    private val application: Application
) : ViewModel() {

    internal val permissionEducationMutableStateFlow = MutableStateFlow(value = PermissionEducationState())
    val permissionEducationStateFlow = permissionEducationMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    fun onGotItButtonClicked() = navigation.pop()

    fun onMoreInfoClicked() = navigation.navigateToUrl(url = application.getString(StringsIds.androidPermissionsUrl))

    private fun educationInfoList(): List<EducationInfo> {
        return listOf(
            EducationInfo(
                title = application.getString(StringsIds.cameraPermission),
                description = application.getString(StringsIds.cameraPermissionExplanation),
                drawableResId = DrawablesIds.camera,
                buttonText = application.getString(StringsIds.gotIt),
                moreInfoVisible = true
            )
        )
    }

    private fun updateState() {
        permissionEducationMutableStateFlow.update { permissionEducationState ->
            permissionEducationState.copy(
                educationInfoList = educationInfoList()
            )
        }
    }
}

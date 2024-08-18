package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.build.type.BuildType
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.isTiramisuOrAbove
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionEducationViewModel(
    private val navigation: PermissionEducationNavigation,
    private val buildType: BuildType,
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
        val isTiramisuOrAbove = isTiramisuOrAbove(sdk = buildType.sdk)

        val readPermissionTitle = if (isTiramisuOrAbove) {
            StringsIds.readMediaImagesPermission
        } else {
            StringsIds.readExternalStoragePermission
        }
        val readPermissionExplanation = if (isTiramisuOrAbove) {
            StringsIds.readMediaImagesPermissionExplanation
        } else {
            StringsIds.readExternalStoragePermissionExplanation
        }

        return listOf(
            EducationInfo(
                title = application.getString(readPermissionTitle),
                description = application.getString(readPermissionExplanation),
                drawableResId = DrawablesIds.gallery,
                buttonText = application.getString(StringsIds.next),
                moreInfoVisible = true
            ),
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

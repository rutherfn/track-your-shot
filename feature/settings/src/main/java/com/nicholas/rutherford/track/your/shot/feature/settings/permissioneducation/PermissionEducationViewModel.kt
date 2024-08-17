package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo
import com.nicholas.rutherford.track.your.shot.feature.splash.DrawablesIds
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.extensions.shouldAskForReadMediaImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PermissionEducationViewModel(
    private val navigation: PermissionEducationNavigation,
    private val application: Application,
) : ViewModel() {

    internal val permissionEducationMutableStateFlow = MutableStateFlow(value = PermissionEducationState())
    val permissionEducationStateFlow = permissionEducationMutableStateFlow.asStateFlow()

    init {
        updateState()
    }

    private fun educationInfoList(): List<EducationInfo> {
        val readPermissionTitle = if (shouldAskForReadMediaImages()) {
            StringsIds.readMediaImagesPermission
        } else {
            StringsIds.readExternalStoragePermission
        }
        val readPermissionExplanation = if (shouldAskForReadMediaImages()) {
            StringsIds.readMediaImagesPermissionExplanation
        } else {
            StringsIds.readExternalStoragePermissionExplanation
        }

        return listOf(
            EducationInfo(
                title = application.getString(readPermissionTitle),
                description = application.getString(readPermissionExplanation),
                drawableResId = DrawablesIds.placeholder,
                buttonText = application.getString(StringsIds.next)
            ),
            EducationInfo(
                title = application.getString(StringsIds.cameraPermission),
                description = application.getString(StringsIds.cameraPermissionExplanation),
                drawableResId = DrawablesIds.placeholder,
                buttonText = application.getString(StringsIds.gotIt)
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
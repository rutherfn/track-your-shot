package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo

data class PermissionEducationState(
    val educationInfoList: List<EducationInfo> = emptyList()
)

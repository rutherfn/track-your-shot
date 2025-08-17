package com.nicholas.rutherford.track.your.shot.feature.settings.permissioneducation

import com.nicholas.rutherford.track.your.shot.data.shared.EducationInfo

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Represents the UI state for the Permission Education screen.
 *
 * @property educationInfoList The list of educational content pages related to permissions,
 * each containing a title, description, image, and optional button text.
 */
data class PermissionEducationState(
    val educationInfoList: List<EducationInfo> = emptyList()
)

package com.nicholas.rutherford.track.my.shot.navigation

import android.os.Parcelable
import androidx.navigation.NavOptions

interface NavigationAction {
    val destination: String
    val parcelableArguments: Map<String, Parcelable>
        get() = emptyMap()
    val navOptions: NavOptions
        get() = NavOptions.Builder().build()
}

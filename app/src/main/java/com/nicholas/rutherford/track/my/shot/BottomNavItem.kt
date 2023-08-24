package com.nicholas.rutherford.track.my.shot

sealed class BottomNavItem {
    object Players : BottomNavItem()
    object Stats : BottomNavItem()
    object Settings : BottomNavItem()
}

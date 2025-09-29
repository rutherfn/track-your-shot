package com.nicholas.rutherford.track.your.shot.compose.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class BaseSnackBar(
    snackBarData: SnackbarData,
) {
}

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
        containerColor = Color(0xFF2E7D32), // Custom background color (dark green)
        contentColor = Color.White, // Custom text color (white)
        actionColor = Color(0xFF81C784) // Custom action button color (light green)
    )
}
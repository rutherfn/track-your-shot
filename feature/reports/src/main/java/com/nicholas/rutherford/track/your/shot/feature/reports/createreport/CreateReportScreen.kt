package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * The main screen composable for creating a new player report.
 *
 * Delegates UI rendering to [CreateReportContent] and passes the required params.
 *
 * @param params Contains UI state and event callbacks for this screen.
 */
@Composable
fun CreateReportScreen(params: CreateReportParams) = CreateReportContent(params = params)

/**
 * UI content of the Create Report screen.
 *
 * Displays a player chooser and a generate report button.
 * Handles runtime permission requests for storage or notifications depending on OS version.
 *
 * @param params Contains UI state and event callbacks for this screen.
 */
@Composable
fun CreateReportContent(params: CreateReportParams) {
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        val allPermissionsGranted = permissionsResult.values.all { it }
        if (allPermissionsGranted) {
            params.attemptToGeneratePlayerReport.invoke()
        } else {
            // TODO: Handle permission denial case (e.g., show rationale or message)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Padding.twenty,
                end = Padding.twenty,
                bottom = Padding.twenty
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerChooser(params = params)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                            permissionsLauncher.launch(
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            )
                        } else {
                            permissionsLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                        }
                    },
                    shape = RoundedCornerShape(size = 50.dp),
                    modifier = Modifier
                        .padding(vertical = Padding.twelve)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Colors.secondaryColor)
                ) {
                    Text(
                        text = stringResource(id = StringsIds.generateReport),
                        style = TextStyles.smallBold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }
}

/**
 * Preview of the Create Report screen content.
 *
 * Uses mock parameters with empty callbacks and default state to display the UI for design inspection.
 */
@Preview(showBackground = true)
@Composable
fun CreateReportContentPreview() {
    CreateReportContent(
        params = CreateReportParams(
            onToolbarMenuClicked = {},
            attemptToGeneratePlayerReport = {},
            onPlayerChanged = {},
            state = CreateReportState()
        )
    )
}

package com.nicholas.rutherford.track.your.shot.feature.reports.createreport

import android.Manifest
import android.content.ContentValues
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nicholas.rutherford.track.your.shot.AppColors
import com.nicholas.rutherford.track.your.shot.base.resources.Colors
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.compose.components.Content
import com.nicholas.rutherford.track.your.shot.data.shared.appbar.AppBar
import com.nicholas.rutherford.track.your.shot.helper.ui.Padding
import com.nicholas.rutherford.track.your.shot.helper.ui.TextStyles
import java.io.ByteArrayOutputStream

@Composable
fun CreateReportScreen(params: CreateReportParams) {
    LaunchedEffect(params.shouldRefreshData) {
        if (params.shouldRefreshData) {
            params.updatePlayersState.invoke()
        }
    }
    Content(
        ui = {
            CreateReportContent(params = params)
        },
        appBar = AppBar(
            toolbarTitle = stringResource(id = StringsIds.createPlayerReport),
            shouldShowMiddleContentAppBar = false,
            shouldIncludeSpaceAfterDeclaration = false,
            shouldShowSecondaryButton = false,
            onIconButtonClicked = { params.onToolbarMenuClicked.invoke() }
        )
    )
}

@Composable
fun CreateReportContent(params: CreateReportParams) {
    val pdfFileName = "example.pdf"
    val mimeType = "application/pdf"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS) // Saves in Documents folder
    }
    val context = LocalContext.current

    val postNotificationPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isPermissionGranted ->
        if (isPermissionGranted) {
            params.showCreatePlayerReportNotification.invoke()
        } else {
        }
    }
//    val permissions = arrayOf(
//        Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//    val permissionsLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { permissionsResult ->
// //        val allPermissionsGranted = permissionsResult.values.all { it }
// //        if (allPermissionsGranted) {
// //            println("have said permissions ")
// //        } else {
// //            println("we dont have permission yet")
// //        }
//    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
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
                        postNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
//                        println("get here test")
//                        if (uri != null) {
//                            resolver.openOutputStream(uri)?.use { outputStream ->
//                                println("get here test2121")
//                                showPdfSavedNotification(context, pdfFileName)
//                                // Generate the PDF content
//                                val pdfContent = generatePdfContent()
//                                outputStream.write(pdfContent)
//                                //      Toast.makeText(context, "PDF saved successfully!", Toast.LENGTH_SHORT).show()
//                            } ?: run {
//                                println("tt")
//                                //    Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            println("tt11")
//                            //     Toast.makeText(context, "Failed to create PDF file", Toast.LENGTH_SHORT).show()
//                        }
                    },
                    shape = RoundedCornerShape(size = 50.dp),
                    modifier = Modifier
                        .padding(vertical = Padding.twelve)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Colors.secondaryColor)
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

private fun generatePdfContent(): ByteArray {
    // Example content for a PDF
    val document = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 400, 1).create()
    val page = document.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint().apply {
        textSize = 16f
    }
    canvas.drawText("Hello, this is a PDF!", 50f, 50f, paint)

    document.finishPage(page)

    // Write the PDF document to a byte array
    val outputStream = ByteArrayOutputStream()
    document.writeTo(outputStream)
    document.close()

    return outputStream.toByteArray()
}

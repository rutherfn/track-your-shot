package com.nicholas.rutherford.track.your.shot.helper.file.generator

import android.annotation.SuppressLint
import android.app.Application
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.Player
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions.SmallForward.toPlayerPositionValue
import com.nicholas.rutherford.track.your.shot.data.room.response.fullName
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseDateValueToString
import java.io.ByteArrayOutputStream

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Implementation of [PdfGenerator] for generating and downloading PDF reports for players.
 *
 * This class uses [PdfDocument] to create PDFs containing player information, shots logged, and related statistics.
 */
class PdfGeneratorImpl(private val application: Application) : PdfGenerator {

    private val resolver: ContentResolver = application.contentResolver

    /**
     * Generates a PDF as a byte array for the given [player].
     *
     * @param player The [Player] object to generate the PDF for.
     * @return Byte array representing the PDF file.
     */
    private fun pdfOutputStreamByteArray(player: Player): ByteArray {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(600, 800, 1).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
            color = Color.BLACK
        }

        val headerPaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
            color = Color.DKGRAY
        }

        val textPaint = Paint().apply {
            textSize = 16f
            color = Color.BLACK
        }

        val dividerPaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 2f
        }

        var yPosition = 50f

        canvas.drawText(application.getString(StringsIds.playerReport), 50f, yPosition, titlePaint)
        yPosition += 40f
        canvas.drawText(application.getString(StringsIds.nameX, player.fullName()), 50f, yPosition, headerPaint)
        yPosition += 30f
        canvas.drawText(application.getString(StringsIds.positionX, player.position.toPlayerPositionValue(application)), 50f, yPosition, textPaint)
        yPosition += 30f
        canvas.drawLine(50f, yPosition, 550f, yPosition, dividerPaint)
        yPosition += 20f

        player.shotsLoggedList.forEachIndexed { index, shot ->
            yPosition += 20f
            canvas.drawText(application.getString(StringsIds.shotX, "${index + 1}: ${shot.shotName}"), 50f, yPosition, headerPaint)
            yPosition += 25f
            canvas.drawText(application.getString(StringsIds.attemptedShotsX, shot.shotsAttempted.toString()), 70f, yPosition, textPaint)
            yPosition += 20f
            canvas.drawText(application.getString(StringsIds.shotsMadeXSecondary, shot.shotsMade.toString()), 70f, yPosition, textPaint)
            yPosition += 20f
            canvas.drawText(application.getString(StringsIds.shotsMissedX, shot.shotsMissed.toString()), 70f, yPosition, textPaint)
            yPosition += 20f
            canvas.drawText(application.getString(StringsIds.shotsMadeAccuracyX, shot.shotsMadePercentValue.toString()), 70f, yPosition, textPaint)
            yPosition += 20f
            canvas.drawText(application.getString(StringsIds.shotsAttemptedDateX, parseDateValueToString(shot.shotsAttemptedMillisecondsValue)), 70f, yPosition, textPaint)
            yPosition += 20f
            canvas.drawText(application.getString(StringsIds.shotsLoggedDateX, parseDateValueToString(shot.shotsLoggedMillisecondsValue)), 70f, yPosition, textPaint)
            yPosition += 20f
            canvas.drawLine(50f, yPosition, 550f, yPosition, dividerPaint)

            // Create new page if current page is full
            if (yPosition > pageInfo.pageHeight - 50f) {
                document.finishPage(page)
                yPosition = 50f
                val newPageInfo = PdfDocument.PageInfo.Builder(600, 800, document.pages.size + 1).create()
                page = document.startPage(newPageInfo)
                canvas = page.canvas
            }
        }

        document.finishPage(page)

        val outputStream = ByteArrayOutputStream()
        document.writeTo(outputStream)
        document.close()

        return outputStream.toByteArray()
    }

    /**
     * Downloads a PDF from the given [url] using the system [DownloadManager].
     *
     * @param url The URL of the PDF to download.
     */
    @SuppressLint("UseKtx")
    override fun downloadPdf(url: String) {
        val download = application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val pdfUri = Uri.parse(url)
        val pdfDownload = DownloadManager.Request(pdfUri)
        pdfDownload.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        download.enqueue(pdfDownload)
    }

    /**
     * Generates a PDF file for the given [player] and saves it with the specified [fileName].
     *
     * @param fileName Name of the PDF file to create.
     * @param player The [Player] object to generate the PDF for.
     * @return A [Pair] containing the [Uri] of the saved PDF (or null if failed) and a result code indicating success or failure.
     */
    override fun generatePlayerPdf(fileName: String, player: Player): Pair<Uri?, Int> {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, Constants.PDF_MIME_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)?.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(pdfOutputStreamByteArray(player))
                return Pair(uri, Constants.PDF_SUCCESSFUL_GENERATE_CODE)
            } ?: return Pair(null, Constants.PDF_CANNOT_SAVE_PDF_CODE)
        } ?: run {
            return Pair(null, Constants.PDF_CANNOT_CREATE_PDF_CODE)
        }
    }
}

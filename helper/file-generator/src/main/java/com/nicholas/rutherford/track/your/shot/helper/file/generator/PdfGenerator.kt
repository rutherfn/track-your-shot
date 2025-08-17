package com.nicholas.rutherford.track.your.shot.helper.file.generator

import android.net.Uri
import com.nicholas.rutherford.track.your.shot.data.room.response.Player

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Defines the contract for PDF generation and downloading.
 */
interface PdfGenerator {

    /**
     * Downloads a PDF file from the given [url].
     *
     * @param url The URL of the PDF to download.
     */
    fun downloadPdf(url: String)

    /**
     * Generates a PDF for the given [player] and saves it with the specified [fileName].
     *
     * @param fileName The name to save the PDF as.
     * @param player The player whose data will be included in the PDF.
     * @return A [Pair] containing the [Uri] of the saved PDF and a status code indicating success or failure.
     */
    fun generatePlayerPdf(fileName: String, player: Player): Pair<Uri?, Int>
}

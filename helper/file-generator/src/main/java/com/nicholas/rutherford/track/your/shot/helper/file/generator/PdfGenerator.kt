package com.nicholas.rutherford.track.your.shot.helper.file.generator

import android.net.Uri
import com.nicholas.rutherford.track.your.shot.data.room.response.Player

interface PdfGenerator {
    fun downloadPdf(url: String)
    fun generatePlayerPdf(fileName: String, player: Player): Pair<Uri?, Int>
}

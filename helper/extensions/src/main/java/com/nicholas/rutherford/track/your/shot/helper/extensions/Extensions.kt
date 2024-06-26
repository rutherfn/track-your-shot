package com.nicholas.rutherford.track.your.shot.helper.extensions

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.ContextCompat
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants.DATE_PATTERN
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

fun Int.toPlayerPositionAbvId(): Int? {
    return when (this) {
        PlayerPositions.PointGuard.value -> {
            StringsIds.pg
        }
        PlayerPositions.ShootingGuard.value -> {
            StringsIds.sg
        }
        PlayerPositions.SmallForward.value -> {
            StringsIds.sf
        }
        PlayerPositions.PowerForward.value -> {
            StringsIds.pf
        }
        PlayerPositions.Center.value -> {
            StringsIds.c
        }
        else -> { null }
    }
}

fun PlayerPositions.toType(): Int {
    return when (this) {
        PlayerPositions.PointGuard -> {
            StringsIds.pointGuard
        }
        PlayerPositions.ShootingGuard -> {
            StringsIds.shootingGuard
        }
        PlayerPositions.SmallForward -> {
            StringsIds.smallForward
        }
        PlayerPositions.PowerForward -> {
            StringsIds.powerForward
        }
        else -> { StringsIds.center }
    }
}

fun String.toLocalDate(): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM d, y", Locale.ENGLISH)
        LocalDate.parse(this, formatter)
    } catch (e: Exception) {
        // Return null if parsing fails
        null
    }
}

fun LocalDate.toDateValue(): String? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM d, y", Locale.ENGLISH)
        this.format(formatter)
    } catch (e: Exception) {
        // Return null if formatting fails
        null
    }
}

fun getImageUri(context: Context, image: Bitmap): Uri? {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "title")
        put(MediaStore.Images.Media.DESCRIPTION, "description")
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    try {
        if (uri != null) {
            val outputStream = contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            outputStream?.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

    return uri
}

fun hasCameraPermissionEnabled(context: Context) = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED

fun hasReadImagePermissionEnabled(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
fun shouldAskForReadMediaImages() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun readMediaImagesOrExternalStoragePermission(): String {
    return if (shouldAskForReadMediaImages()) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

fun parseValueToDate(value: String): Date? =
    SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).parse(value)

fun parseDateValueToString(timeInMilliseconds: Long): String =
    SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(Date(timeInMilliseconds))

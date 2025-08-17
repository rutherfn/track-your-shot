package com.nicholas.rutherford.track.your.shot.helper.extensions

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.PlayerPositions
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants.DATE_PATTERN
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Collection of extension and helper functions for safe operations, date parsing/formatting,
 * player position conversions, image handling, and permission checks.
 */

/** Safe call with two non-null parameters */
inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) {
        block(p1, p2)
    } else {
        null
    }
}

/** Safe call with three non-null parameters */
inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) {
        block(p1, p2, p3)
    } else {
        null
    }
}

/** Safe call with four non-null parameters */
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) {
        block(p1, p2, p3, p4)
    } else {
        null
    }
}

/** Safe call with five non-null parameters */
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) {
        block(p1, p2, p3, p4, p5)
    } else {
        null
    }
}

/**
 * Converts an integer representing a player position to its abbreviated string resource ID.
 *
 * @return The corresponding StringsIds resource or null if no match.
 */
fun Int.toPlayerPositionAbvId(): Int? {
    return when (this) {
        PlayerPositions.PointGuard.value -> StringsIds.pg
        PlayerPositions.ShootingGuard.value -> StringsIds.sg
        PlayerPositions.SmallForward.value -> StringsIds.sf
        PlayerPositions.PowerForward.value -> StringsIds.pf
        PlayerPositions.Center.value -> StringsIds.c
        else -> null
    }
}

/**
 * Converts a PlayerPositions enum to its corresponding type resource ID.
 *
 * @return The StringsIds resource corresponding to the player position.
 */
fun PlayerPositions.toType(): Int {
    return when (this) {
        PlayerPositions.PointGuard -> StringsIds.pointGuard
        PlayerPositions.ShootingGuard -> StringsIds.shootingGuard
        PlayerPositions.SmallForward -> StringsIds.smallForward
        PlayerPositions.PowerForward -> StringsIds.powerForward
        else -> StringsIds.center
    }
}

/** Converts a [Date] object to a timestamp string using "MM/dd/yyyy" format. */
fun Date.toTimestampString(): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(this)
}

/** Converts a string to [LocalDate] using "MMM d, y" pattern; logs error if parsing fails. */
fun String.toLocalDate(): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM d, y", Locale.ENGLISH)
        LocalDate.parse(this, formatter)
    } catch (e: Exception) {
        Log.e("DateParsing", "Failed to parse date string: $this", e)
        null
    }
}

/** Converts a [LocalDate] to a string using "MMM d, y" pattern; logs error if formatting fails. */
fun LocalDate.toDateValue(): String? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM d, y", Locale.ENGLISH)
        this.format(formatter)
    } catch (e: Exception) {
        Log.e("DateFormatting", "Failed to format LocalDate: $this", e)
        null
    }
}

/** Normalizes multiple consecutive spaces in a string to a single space. */
fun String.normalizeSpaces(): String =
    trim().replace(Regex("\\s+"), " ")

/**
 * Saves a [Bitmap] image to the MediaStore and returns its [Uri].
 *
 * @return The [Uri] of the saved image, or null if saving failed.
 */
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

/** Checks if the camera permission is granted in the current [context]. */
fun hasCameraPermissionEnabled(context: Context) = ContextCompat.checkSelfPermission(
    context,
    Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED

/** Parses a string value to a [Date] using the app-defined DATE_PATTERN. */
fun parseValueToDate(value: String): Date? =
    SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).parse(value)

/** Formats a time in milliseconds to a string using the app-defined DATE_PATTERN. */
fun parseDateValueToString(timeInMilliseconds: Long): String =
    SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(Date(timeInMilliseconds))

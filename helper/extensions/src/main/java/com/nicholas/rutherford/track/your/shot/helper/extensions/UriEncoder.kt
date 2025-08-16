package com.nicholas.rutherford.track.your.shot.helper.extensions

import android.net.Uri

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16.
 *
 * Utility object responsible for encoding strings for use in URIs.
 *
 * This wrapper around [Uri.encode] allows for easier testing and potential customization of URI encoding logic.
 */
object UriEncoder {

    /**
     * Encodes the given [value] to make it safe for use in a URI.
     *
     * @param value The string to encode.
     * @return The URI-encoded string.
     */
    fun encode(value: String): String = Uri.encode(value)
}

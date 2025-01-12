package com.nicholas.rutherford.track.your.shot.helper.extensions

import java.util.Date

class DateExtImpl : DateExt {
    override val now: Long = Date().time
}

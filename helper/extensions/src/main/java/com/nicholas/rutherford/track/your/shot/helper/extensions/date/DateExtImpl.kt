package com.nicholas.rutherford.track.your.shot.helper.extensions.date

import java.util.Date

class DateExtImpl : DateExt {
    override val now: Long = Date().time
}

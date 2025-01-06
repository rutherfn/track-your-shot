package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toIndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

class TestIndividualPlayerReport {

    fun build(): IndividualPlayerReport = TestIndividualPlayerReportEntity.build().toIndividualPlayerReport()
}

package com.nicholas.rutherford.track.your.shot.data.test.room

import com.nicholas.rutherford.track.your.shot.data.room.entities.toIndividualPlayerReport
import com.nicholas.rutherford.track.your.shot.data.room.response.IndividualPlayerReport

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * Utility class for creating test instances of [IndividualPlayerReport].
 * Converts test entities from [TestIndividualPlayerReportEntity] into domain models.
 */
class TestIndividualPlayerReport {

    /**
     * Builds a test [IndividualPlayerReport] instance.
     *
     * @return a new [IndividualPlayerReport] converted from [TestIndividualPlayerReportEntity].
     */
    fun build(): IndividualPlayerReport = TestIndividualPlayerReportEntity.build().toIndividualPlayerReport()
}

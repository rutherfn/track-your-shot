package com.nicholas.rutherford.track.your.shot.feature.players.shots.logshot.extension

import android.app.Application
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.data.room.response.ShotLogged
import com.nicholas.rutherford.track.your.shot.data.room.response.isTheSame
import com.nicholas.rutherford.track.your.shot.data.shared.alert.Alert
import com.nicholas.rutherford.track.your.shot.data.shared.alert.AlertConfirmAndDismissButton
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import com.nicholas.rutherford.track.your.shot.helper.extensions.parseValueToDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class LogShotViewModelExtImpl(
    private val application: Application,
    private val scope: CoroutineScope
) : LogShotViewModelExt {
    override var logShotInfo = LogShotInfo()

    internal fun shotsPercentValue(
        shotsMade: Double,
        shotsMissed: Double,
        isShotsMade: Boolean
    ): Double {
        val totalShots = if (isShotsMade) {
            shotsMade + shotsMissed
        } else {
            shotsMissed + shotsMade
        }
        val percentValue =
            if (shotsMade != Constants.SHOT_ZERO_VALUE && shotsMissed != Constants.SHOT_ZERO_VALUE) {
                if (isShotsMade) {
                    shotsMade / totalShots * 100
                } else {
                    shotsMissed / totalShots * 100
                }
            } else {
                Constants.SHOT_ZERO_VALUE
            }

        return percentValue
    }

    override fun setInitialInfo(logShotInfo: LogShotInfo) {
        this.logShotInfo = logShotInfo
    }

    override fun shotsAttempted(shotsMade: Int, shotsMissed: Int): Int {
        return if (shotsMade != 0 || shotsMissed != 0) {
            shotsMade + shotsMissed
        } else {
            0
        }
    }

    override fun percentageFormat(
        shotsMade: Double,
        shotsMissed: Double,
        isShotsMade: Boolean
    ): String {
        val percentage = shotsPercentValue(
            shotsMade = shotsMade,
            shotsMissed = shotsMissed,
            isShotsMade = isShotsMade
        )

        return if (percentage == Constants.SHOT_ZERO_VALUE) {
            application.getString(StringsIds.empty)
        } else {
            val locale = Locale("en", "US")
            val percentageRoundedValue = String.format(locale, "%.1f", percentage)
            if (percentageRoundedValue.endsWith(".0")) {
                application.getString(
                    StringsIds.shotPercentage,
                    percentageRoundedValue.substring(0, percentageRoundedValue.length - 2)
                )
            } else {
                application.getString(StringsIds.shotPercentage, percentageRoundedValue)
            }
        }
    }

    override fun filterShotsById(shots: List<ShotLogged>): List<ShotLogged> = shots.filter { it.id != logShotInfo.shotId }

    override fun convertPercentageToDouble(percentage: String): Double {
        if (!percentage.contains("%")) {
            return 0.0
        }

        val valueWithoutPercentSign = percentage.replace("%", "")

        val valueWithDecimal = if (!valueWithoutPercentSign.contains(".")) {
            "$valueWithoutPercentSign.0"
        } else {
            valueWithoutPercentSign
        }

        return try {
            valueWithDecimal.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    override fun convertValueToDate(value: String): Date? {
        return if (value.isEmpty()) {
            null
        } else {
            parseValueToDate(value = value)
        }
    }

    override fun invalidLogShotAlert(description: String): Alert {
        return Alert(
            title = application.getString(StringsIds.error),
            dismissButton = AlertConfirmAndDismissButton(buttonText = application.getString(StringsIds.gotIt)),
            description = description
        )
    }

    override fun deleteShotAlert(onYesDeleteShot: suspend () -> Unit, shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.deleteShot),
            description = application.getString(StringsIds.areYouSureYouWantToDeleteXShot, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.yes),
                onButtonClicked = { scope.launch { onYesDeleteShot() } }
            ),
            dismissButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.no)
            )
        )
    }

    override fun deleteShotConfirmAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotHasBeenDeleted),
            description = application.getString(StringsIds.xShotHasBeenRemovedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    override fun deleteShotErrorAlert(shotName: String): Alert {
        return Alert(
            title = application.getString(StringsIds.shotHasNotBeenDeleted),
            description = application.getString(StringsIds.xShotHasNotBeenRemovedDescription, shotName),
            confirmButton = AlertConfirmAndDismissButton(
                buttonText = application.getString(StringsIds.gotIt)
            )
        )
    }

    override fun noChangesForShotAlert(initialShotLogged: ShotLogged?, pendingShotLogged: ShotLogged): Alert? {
        initialShotLogged?.let { currentShot ->
            if (currentShot.isTheSame(pendingShotLogged)) {
                return Alert(
                    title = application.getString(StringsIds.noChangesMade),
                    dismissButton = AlertConfirmAndDismissButton(
                        buttonText = application.getString(StringsIds.gotIt)
                    ),
                    description = application.getString(StringsIds.currentShotHasNoChangesDescription)
                )
            } else {
                return null
            }
        } ?: return null
    }

    override fun shotEntryInvalidAlert(
        shotsMade: Int,
        shotsMissed: Int,
        shotsAttemptedMillisecondsValue: Long
    ): Alert? {
        val description: String? = if (shotsMade == 0) {
            application.getString(StringsIds.shotsNotRecordedDescription)
        } else if (shotsMissed == 0) {
            application.getString(StringsIds.missedShotsNotRecordedDescription)
        } else if (shotsAttemptedMillisecondsValue == 0L) {
            application.getString(StringsIds.dateShotWasTakenDescription)
        } else {
            null
        }

        description?.let { desc ->
            return Alert(
                title = application.getString(StringsIds.noChangesMade),
                dismissButton = AlertConfirmAndDismissButton(
                    buttonText = application.getString(StringsIds.gotIt)
                ),
                description = desc
            )
        } ?: run {
            return null
        }
    }
}

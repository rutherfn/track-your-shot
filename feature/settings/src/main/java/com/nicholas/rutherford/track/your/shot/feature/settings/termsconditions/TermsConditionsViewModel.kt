package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.feature.splash.StringsIds
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TermsConditionsViewModel(
    private val navigation: TermsConditionsNavigation,
    private val application: Application,
    private val createSharedPreferences: CreateSharedPreferences
) : ViewModel() {

    internal var termsConditionsMutableStateFlow = MutableStateFlow(value = TermsConditionsState())
    val termsConditionsStateFlow = termsConditionsMutableStateFlow.asStateFlow()

    init {
        updateInfoListState()
    }

    fun buildInfoList(): List<TermsConditionInfo> {
        return listOf(
            TermsConditionInfo(
                title = application.getString(StringsIds.introduction),
                description = application.getString(StringsIds.termsConditionsDescription)
            ),
            TermsConditionInfo(
                title = application.getString(StringsIds.accounts),
                description = application.getString(StringsIds.accountSecurityDescription)

            ),
            TermsConditionInfo(
                title = application.getString(StringsIds.otherResources),
                description = application.getString(StringsIds.otherResourcesDescription)
            ),
            TermsConditionInfo(
                title = application.getString(StringsIds.acceptanceOfTheseTerms),
                description = application.getString(StringsIds.acceptanceOfTheseTermsDescription)
            ),
            TermsConditionInfo(
                title = application.getString(StringsIds.contactingUs),
                description = application.getString(StringsIds.contactingUsDescription)
            )
        )
    }

    fun updateInfoListState() {
        termsConditionsMutableStateFlow.update {
            it.copy(
                infoList = buildInfoList()
            )
        }
    }

    fun updateButtonTextState(isAcknowledgeConditions: Boolean) {
        termsConditionsMutableStateFlow.update {
            it.copy(
                buttonText = if (isAcknowledgeConditions) {
                    application.getString(StringsIds.acknowledgeAndAgreeToTerms)
                } else {
                    application.getString(StringsIds.close)
                }
            )
        }
    }

    fun onCloseAcceptButtonClicked(isAcknowledgeConditions: Boolean) {
        if (isAcknowledgeConditions) {
            createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = false)
            navigation.navigateToPlayerList()
        } else {
            navigation.navigateToSettings()
        }
    }
}
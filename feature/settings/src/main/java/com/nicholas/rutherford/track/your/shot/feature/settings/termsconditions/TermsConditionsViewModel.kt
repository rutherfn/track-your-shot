package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import android.app.Application
import androidx.lifecycle.ViewModel
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DELAY_BEFORE_ONBOARDING = 750L

class TermsConditionsViewModel(
    private val navigation: TermsConditionsNavigation,
    private val application: Application,
    private val createSharedPreferences: CreateSharedPreferences,
    private val scope: CoroutineScope
) : ViewModel() {

    internal var termsConditionsMutableStateFlow = MutableStateFlow(value = TermsConditionsState())
    val termsConditionsStateFlow = termsConditionsMutableStateFlow.asStateFlow()

    init {
        updateInfoListState()
    }

    internal fun buildInfoList(): List<TermsConditionInfo> {
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
            scope.launch {
                createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = false)
                navigation.navigateToPlayerList()

                delay(DELAY_BEFORE_ONBOARDING)
                navigation.navigateToOnboarding()
            }
        } else {
            navigation.navigateToSettings()
        }
    }

    fun onDevEmailClicked() {
        navigation.navigateToDevEmail(email = application.getString(StringsIds.devEmail))
    }
}

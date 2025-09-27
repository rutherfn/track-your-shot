package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.base.vm.BaseViewModel
import com.nicholas.rutherford.track.your.shot.data.store.writer.DataStorePreferencesWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DELAY_BEFORE_ONBOARDING = 750L

/**
 * Created by Nicholas Rutherford, last edited on 2025-08-16
 *
 * ViewModel for managing the Terms & Conditions screen state and interactions.
 *
 * Responsibilities include:
 * - Building and updating the list of terms sections displayed to the user.
 * - Handling button text based on whether terms need to be accepted.
 * - Navigating back, accepting terms, or sending user to the appropriate screens.
 *
 * @param savedStateHandle Used to retrieve arguments passed to the screen (e.g., `shouldAcceptTerms`).
 * @param navigation Interface that defines navigation actions for this screen.
 * @param application Provides access to localized string resources.
 * @param dataStorePreferencesWriter Handles writing to the DataStore preferences.
 * @param scope Coroutine scope for launching background tasks.
 */
class TermsConditionsViewModel(
    savedStateHandle: SavedStateHandle,
    private val navigation: TermsConditionsNavigation,
    private val application: Application,
    private val dataStorePreferencesWriter: DataStorePreferencesWriter,
    private val scope: CoroutineScope
) : BaseViewModel() {

    private var termsConditionsMutableStateFlow = MutableStateFlow(value = TermsConditionsState())
    val termsConditionsStateFlow = termsConditionsMutableStateFlow.asStateFlow()

    internal val shouldAcceptTermsParam: Boolean = savedStateHandle.get<Boolean>("shouldAcceptTerms") ?: false

    init {
        updateInfoListState()
        updateButtonTextState()
    }

    /**
     * Builds and returns the list of terms and conditions sections.
     */
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

    /**
     * Updates the state flow with the full list of terms and conditions sections.
     */
    fun updateInfoListState() {
        termsConditionsMutableStateFlow.update {
            it.copy(
                infoList = buildInfoList()
            )
        }
    }

    /**
     * Updates the button text shown at the bottom of the screen,
     * depending on whether the user is expected to accept terms.
     */
    fun updateButtonTextState() {
        termsConditionsMutableStateFlow.update {
            it.copy(
                buttonText = if (shouldAcceptTermsParam) {
                    application.getString(StringsIds.acknowledgeAndAgreeToTerms)
                } else {
                    application.getString(StringsIds.close)
                }
            )
        }
    }

    /**
     * Called when the back button is pressed.
     *
     * Navigates to the appropriate screen based on whether terms need to be accepted.
     */
    fun onBackClicked() {
        if (shouldAcceptTermsParam) {
            navigation.finish()
        } else {
            navigation.navigateToSettings()
        }
    }

    /**
     * Called when the close/accept button is clicked.
     *
     * - If user must accept terms: saves acceptance to preferences and navigates to onboarding.
     * - Otherwise, simply returns to the settings screen.
     */
    fun onCloseAcceptButtonClicked() {
        if (shouldAcceptTermsParam) {
            scope.launch {
                dataStorePreferencesWriter.saveShouldShowTermsAndConditions(value = false)
                navigation.navigateToPlayerList()

                delay(DELAY_BEFORE_ONBOARDING)
                navigation.navigateToOnboarding()
            }
        } else {
            navigation.navigateToSettings()
        }
    }

    /**
     * Called when the user taps the developer support email link.
     * Navigates to the user's email client with pre-filled developer email.
     */
    fun onDevEmailClicked() {
        navigation.navigateToDevEmail(email = application.getString(StringsIds.devEmail))
    }
}

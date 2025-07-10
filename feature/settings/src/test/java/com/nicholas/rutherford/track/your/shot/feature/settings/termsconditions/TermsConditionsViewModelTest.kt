package com.nicholas.rutherford.track.your.shot.feature.settings.termsconditions

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.nicholas.rutherford.track.your.shot.base.resources.StringsIds
import com.nicholas.rutherford.track.your.shot.shared.preference.create.CreateSharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TermsConditionsViewModelTest {

    private lateinit var termsConditionsViewModel: TermsConditionsViewModel

    private var savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    private var navigation = mockk<TermsConditionsNavigation>(relaxed = true)

    private val application = mockk<Application>(relaxed = true)

    private val createSharedPreferences = mockk<CreateSharedPreferences>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun beforeEach() {
        Dispatchers.setMain(dispatcher)

        every { application.getString(StringsIds.introduction) } returns "Introduction"
        every { application.getString(StringsIds.termsConditionsDescription) } returns "These terms and conditions (&quot;Agreement&quot;) set forth the general terms and conditions of your use of the &quot;Track Your Shot&quot; mobile application (&quot;Mobile Application&quot; or &quot;Service&quot;) and any of its related products and services (collectively, &quot;Services&quot;). This Agreement is legally binding between you (&quot;User&quot;, &quot;you&quot; or &quot;your&quot;) and Track Your Shot (&quot;Track Your Shot&quot;, &quot;we&quot;, &quot;us&quot; or &quot;our&quot;). If you are entering into this Agreement on behalf of a business or other legal entity, you represent that you have the authority to bind such entity to this Agreement, in which case the terms &quot;User&quot;, &quot;you&quot; or &quot;your&quot; shall refer to such entity. If you do not have such authority, or if you do not agree with the terms of this Agreement, you must not accept this Agreement and may not access and use the Mobile Application and Services. By accessing and using the Mobile Application and Services, you acknowledge that you have read, understood, and agree to be bound by the terms of this Agreement. You acknowledge that this Agreement is a contract between you and Track Your Shot, even though it is electronic and is not physically signed by you, and it governs your use of the Mobile Application and Services."
        every { application.getString(StringsIds.accounts) } returns "Accounts"
        every { application.getString(StringsIds.accountSecurityDescription) } returns "If you create an account in the Mobile Application, you are responsible for maintaining the security of your account and you are fully responsible for all activities that occur under the account and any other actions taken in connection with it. We may monitor and review new accounts before you may sign in and start using the Services. Providing false contact information of any kind may result in the termination of your account. You must immediately notify us of any unauthorized uses of your account or any other breaches of security. We will not be liable for any acts or omissions by you, including any damages of any kind incurred as a result of such acts or omissions. We may suspend, disable, or delete your account (or any part thereof) if we determine that you have violated any provision of this Agreement or that your conduct or content would tend to damage our reputation and goodwill. If we delete your account for the foregoing reasons, you may not re-register for our Services. We may block your email address and Internet protocol address to prevent further registration."
        every { application.getString(StringsIds.otherResources) } returns "Other Resources"
        every { application.getString(StringsIds.otherResourcesDescription) } returns "Although the Mobile Application and Services may link to other resources (such as websites, mobile applications, etc.), we are not, directly or indirectly, implying any approval, association, sponsorship, endorsement, or affiliation with any linked resource, unless specifically stated herein. We are not responsible for examining or evaluating, and we do not warrant the offerings of, any businesses or individuals or the content of their resources. We do not assume any responsibility or liability for the actions, products, services, and content of any other third parties. You should carefully review the legal statements and other conditions of use of any resource which you access through a link in the Mobile Application. Your linking to any other off-site resources is at your own risk."
        every { application.getString(StringsIds.acceptanceOfTheseTerms) } returns "Acceptance Of These Terms"
        every { application.getString(StringsIds.acceptanceOfTheseTermsDescription) } returns "<![CDATA[You acknowledge that you have read this Agreement and agree to all its terms and conditions. By accessing and using the Mobile Application and Services, you agree to be bound by this Agreement. If you do not agree to abide by the terms of this Agreement, you are not authorized to access or use the Mobile Application and Services.]]>"
        every { application.getString(StringsIds.contactingUs) } returns "Contacting Us"
        every { application.getString(StringsIds.contactingUsDescription) } returns "If you have any questions, concerns, or complaints regarding this Agreement, we encourage you to contact us with the email below: "
        every { application.getString(StringsIds.close) } returns "Close"
        every { application.getString(StringsIds.devEmail) } returns "nicholasrutherforddeveloper@gmail.com"
        every { application.getString(StringsIds.thisDocumentWasLastUpdatedOn) } returns "This document was last updated on November 9, 2024"
        every { application.getString(StringsIds.acknowledgeAndAgreeToTerms) } returns "Acknowledge / Agree To Terms"

        every { savedStateHandle.get<Boolean>("shouldAcceptTerms") } returns false

        termsConditionsViewModel = TermsConditionsViewModel(
            savedStateHandle = savedStateHandle,
            navigation = navigation,
            application = application,
            createSharedPreferences = createSharedPreferences,
            scope = scope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun afterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun constants() {
        Assertions.assertEquals(DELAY_BEFORE_ONBOARDING, 750L)
    }

    @Test
    fun `build info list should return back default info list`() {
        val result = termsConditionsViewModel.buildInfoList()

        Assertions.assertEquals(
            result,
            listOf(
                TermsConditionInfo(
                    title = "Introduction",
                    description = "These terms and conditions (&quot;Agreement&quot;) set forth the general terms and conditions of your use of the &quot;Track Your Shot&quot; mobile application (&quot;Mobile Application&quot; or &quot;Service&quot;) and any of its related products and services (collectively, &quot;Services&quot;). This Agreement is legally binding between you (&quot;User&quot;, &quot;you&quot; or &quot;your&quot;) and Track Your Shot (&quot;Track Your Shot&quot;, &quot;we&quot;, &quot;us&quot; or &quot;our&quot;). If you are entering into this Agreement on behalf of a business or other legal entity, you represent that you have the authority to bind such entity to this Agreement, in which case the terms &quot;User&quot;, &quot;you&quot; or &quot;your&quot; shall refer to such entity. If you do not have such authority, or if you do not agree with the terms of this Agreement, you must not accept this Agreement and may not access and use the Mobile Application and Services. By accessing and using the Mobile Application and Services, you acknowledge that you have read, understood, and agree to be bound by the terms of this Agreement. You acknowledge that this Agreement is a contract between you and Track Your Shot, even though it is electronic and is not physically signed by you, and it governs your use of the Mobile Application and Services."
                ),
                TermsConditionInfo(
                    title = "Accounts",
                    description = "If you create an account in the Mobile Application, you are responsible for maintaining the security of your account and you are fully responsible for all activities that occur under the account and any other actions taken in connection with it. We may monitor and review new accounts before you may sign in and start using the Services. Providing false contact information of any kind may result in the termination of your account. You must immediately notify us of any unauthorized uses of your account or any other breaches of security. We will not be liable for any acts or omissions by you, including any damages of any kind incurred as a result of such acts or omissions. We may suspend, disable, or delete your account (or any part thereof) if we determine that you have violated any provision of this Agreement or that your conduct or content would tend to damage our reputation and goodwill. If we delete your account for the foregoing reasons, you may not re-register for our Services. We may block your email address and Internet protocol address to prevent further registration."
                ),
                TermsConditionInfo(
                    title = "Other Resources",
                    description = "Although the Mobile Application and Services may link to other resources (such as websites, mobile applications, etc.), we are not, directly or indirectly, implying any approval, association, sponsorship, endorsement, or affiliation with any linked resource, unless specifically stated herein. We are not responsible for examining or evaluating, and we do not warrant the offerings of, any businesses or individuals or the content of their resources. We do not assume any responsibility or liability for the actions, products, services, and content of any other third parties. You should carefully review the legal statements and other conditions of use of any resource which you access through a link in the Mobile Application. Your linking to any other off-site resources is at your own risk."
                ),
                TermsConditionInfo(
                    title = "Acceptance Of These Terms",
                    description = "<![CDATA[You acknowledge that you have read this Agreement and agree to all its terms and conditions. By accessing and using the Mobile Application and Services, you agree to be bound by this Agreement. If you do not agree to abide by the terms of this Agreement, you are not authorized to access or use the Mobile Application and Services.]]>"
                ),
                TermsConditionInfo(
                    title = "Contacting Us",
                    description = "If you have any questions, concerns, or complaints regarding this Agreement, we encourage you to contact us with the email below: "
                )
            )
        )
    }

    @Test
    fun `update info list state should update state`() {
        every { savedStateHandle.get<Boolean>("shouldAcceptTerms") } returns true

        termsConditionsViewModel = TermsConditionsViewModel(
            savedStateHandle = savedStateHandle,
            navigation = navigation,
            application = application,
            createSharedPreferences = createSharedPreferences,
            scope = scope
        )

        termsConditionsViewModel.updateInfoListState()

        val result = termsConditionsViewModel.termsConditionsStateFlow.value

        Assertions.assertEquals(
            result,
            TermsConditionsState(
                infoList = listOf(
                    TermsConditionInfo(
                        title = "Introduction",
                        description = "These terms and conditions (&quot;Agreement&quot;) set forth the general terms and conditions of your use of the &quot;Track Your Shot&quot; mobile application (&quot;Mobile Application&quot; or &quot;Service&quot;) and any of its related products and services (collectively, &quot;Services&quot;). This Agreement is legally binding between you (&quot;User&quot;, &quot;you&quot; or &quot;your&quot;) and Track Your Shot (&quot;Track Your Shot&quot;, &quot;we&quot;, &quot;us&quot; or &quot;our&quot;). If you are entering into this Agreement on behalf of a business or other legal entity, you represent that you have the authority to bind such entity to this Agreement, in which case the terms &quot;User&quot;, &quot;you&quot; or &quot;your&quot; shall refer to such entity. If you do not have such authority, or if you do not agree with the terms of this Agreement, you must not accept this Agreement and may not access and use the Mobile Application and Services. By accessing and using the Mobile Application and Services, you acknowledge that you have read, understood, and agree to be bound by the terms of this Agreement. You acknowledge that this Agreement is a contract between you and Track Your Shot, even though it is electronic and is not physically signed by you, and it governs your use of the Mobile Application and Services."
                    ),
                    TermsConditionInfo(
                        title = "Accounts",
                        description = "If you create an account in the Mobile Application, you are responsible for maintaining the security of your account and you are fully responsible for all activities that occur under the account and any other actions taken in connection with it. We may monitor and review new accounts before you may sign in and start using the Services. Providing false contact information of any kind may result in the termination of your account. You must immediately notify us of any unauthorized uses of your account or any other breaches of security. We will not be liable for any acts or omissions by you, including any damages of any kind incurred as a result of such acts or omissions. We may suspend, disable, or delete your account (or any part thereof) if we determine that you have violated any provision of this Agreement or that your conduct or content would tend to damage our reputation and goodwill. If we delete your account for the foregoing reasons, you may not re-register for our Services. We may block your email address and Internet protocol address to prevent further registration."
                    ),
                    TermsConditionInfo(
                        title = "Other Resources",
                        description = "Although the Mobile Application and Services may link to other resources (such as websites, mobile applications, etc.), we are not, directly or indirectly, implying any approval, association, sponsorship, endorsement, or affiliation with any linked resource, unless specifically stated herein. We are not responsible for examining or evaluating, and we do not warrant the offerings of, any businesses or individuals or the content of their resources. We do not assume any responsibility or liability for the actions, products, services, and content of any other third parties. You should carefully review the legal statements and other conditions of use of any resource which you access through a link in the Mobile Application. Your linking to any other off-site resources is at your own risk."
                    ),
                    TermsConditionInfo(
                        title = "Acceptance Of These Terms",
                        description = "<![CDATA[You acknowledge that you have read this Agreement and agree to all its terms and conditions. By accessing and using the Mobile Application and Services, you agree to be bound by this Agreement. If you do not agree to abide by the terms of this Agreement, you are not authorized to access or use the Mobile Application and Services.]]>"
                    ),
                    TermsConditionInfo(
                        title = "Contacting Us",
                        description = "If you have any questions, concerns, or complaints regarding this Agreement, we encourage you to contact us with the email below: "
                    )
                ),
                buttonText = "Acknowledge / Agree To Terms"
            )
        )
    }

    @Nested
    inner class UpdateButtonTextState {

        @Test
        fun `when shouldAcceptTerms is set to true should update button state`() {
            every { savedStateHandle.get<Boolean>("shouldAcceptTerms") } returns true

            termsConditionsViewModel = TermsConditionsViewModel(
                savedStateHandle = savedStateHandle,
                navigation = navigation,
                application = application,
                createSharedPreferences = createSharedPreferences,
                scope = scope
            )

            termsConditionsViewModel.updateButtonTextState()

            val result = termsConditionsViewModel.termsConditionsStateFlow.value

            Assertions.assertEquals(
                result,
                TermsConditionsState(
                    infoList = listOf(
                        TermsConditionInfo(
                            title = "Introduction",
                            description = "These terms and conditions (&quot;Agreement&quot;) set forth the general terms and conditions of your use of the &quot;Track Your Shot&quot; mobile application (&quot;Mobile Application&quot; or &quot;Service&quot;) and any of its related products and services (collectively, &quot;Services&quot;). This Agreement is legally binding between you (&quot;User&quot;, &quot;you&quot; or &quot;your&quot;) and Track Your Shot (&quot;Track Your Shot&quot;, &quot;we&quot;, &quot;us&quot; or &quot;our&quot;). If you are entering into this Agreement on behalf of a business or other legal entity, you represent that you have the authority to bind such entity to this Agreement, in which case the terms &quot;User&quot;, &quot;you&quot; or &quot;your&quot; shall refer to such entity. If you do not have such authority, or if you do not agree with the terms of this Agreement, you must not accept this Agreement and may not access and use the Mobile Application and Services. By accessing and using the Mobile Application and Services, you acknowledge that you have read, understood, and agree to be bound by the terms of this Agreement. You acknowledge that this Agreement is a contract between you and Track Your Shot, even though it is electronic and is not physically signed by you, and it governs your use of the Mobile Application and Services."
                        ),
                        TermsConditionInfo(
                            title = "Accounts",
                            description = "If you create an account in the Mobile Application, you are responsible for maintaining the security of your account and you are fully responsible for all activities that occur under the account and any other actions taken in connection with it. We may monitor and review new accounts before you may sign in and start using the Services. Providing false contact information of any kind may result in the termination of your account. You must immediately notify us of any unauthorized uses of your account or any other breaches of security. We will not be liable for any acts or omissions by you, including any damages of any kind incurred as a result of such acts or omissions. We may suspend, disable, or delete your account (or any part thereof) if we determine that you have violated any provision of this Agreement or that your conduct or content would tend to damage our reputation and goodwill. If we delete your account for the foregoing reasons, you may not re-register for our Services. We may block your email address and Internet protocol address to prevent further registration."
                        ),
                        TermsConditionInfo(
                            title = "Other Resources",
                            description = "Although the Mobile Application and Services may link to other resources (such as websites, mobile applications, etc.), we are not, directly or indirectly, implying any approval, association, sponsorship, endorsement, or affiliation with any linked resource, unless specifically stated herein. We are not responsible for examining or evaluating, and we do not warrant the offerings of, any businesses or individuals or the content of their resources. We do not assume any responsibility or liability for the actions, products, services, and content of any other third parties. You should carefully review the legal statements and other conditions of use of any resource which you access through a link in the Mobile Application. Your linking to any other off-site resources is at your own risk."
                        ),
                        TermsConditionInfo(
                            title = "Acceptance Of These Terms",
                            description = "<![CDATA[You acknowledge that you have read this Agreement and agree to all its terms and conditions. By accessing and using the Mobile Application and Services, you agree to be bound by this Agreement. If you do not agree to abide by the terms of this Agreement, you are not authorized to access or use the Mobile Application and Services.]]>"
                        ),
                        TermsConditionInfo(
                            title = "Contacting Us",
                            description = "If you have any questions, concerns, or complaints regarding this Agreement, we encourage you to contact us with the email below: "
                        )
                    ),
                    buttonText = "Acknowledge / Agree To Terms"
                )
            )
        }

        @Test
        fun `when isAcknowledgeConditions is set to false should update button state`() {
            termsConditionsViewModel.updateButtonTextState()

            val result = termsConditionsViewModel.termsConditionsStateFlow.value

            Assertions.assertEquals(
                result,
                TermsConditionsState(
                    infoList = listOf(
                        TermsConditionInfo(
                            title = "Introduction",
                            description = "These terms and conditions (&quot;Agreement&quot;) set forth the general terms and conditions of your use of the &quot;Track Your Shot&quot; mobile application (&quot;Mobile Application&quot; or &quot;Service&quot;) and any of its related products and services (collectively, &quot;Services&quot;). This Agreement is legally binding between you (&quot;User&quot;, &quot;you&quot; or &quot;your&quot;) and Track Your Shot (&quot;Track Your Shot&quot;, &quot;we&quot;, &quot;us&quot; or &quot;our&quot;). If you are entering into this Agreement on behalf of a business or other legal entity, you represent that you have the authority to bind such entity to this Agreement, in which case the terms &quot;User&quot;, &quot;you&quot; or &quot;your&quot; shall refer to such entity. If you do not have such authority, or if you do not agree with the terms of this Agreement, you must not accept this Agreement and may not access and use the Mobile Application and Services. By accessing and using the Mobile Application and Services, you acknowledge that you have read, understood, and agree to be bound by the terms of this Agreement. You acknowledge that this Agreement is a contract between you and Track Your Shot, even though it is electronic and is not physically signed by you, and it governs your use of the Mobile Application and Services."
                        ),
                        TermsConditionInfo(
                            title = "Accounts",
                            description = "If you create an account in the Mobile Application, you are responsible for maintaining the security of your account and you are fully responsible for all activities that occur under the account and any other actions taken in connection with it. We may monitor and review new accounts before you may sign in and start using the Services. Providing false contact information of any kind may result in the termination of your account. You must immediately notify us of any unauthorized uses of your account or any other breaches of security. We will not be liable for any acts or omissions by you, including any damages of any kind incurred as a result of such acts or omissions. We may suspend, disable, or delete your account (or any part thereof) if we determine that you have violated any provision of this Agreement or that your conduct or content would tend to damage our reputation and goodwill. If we delete your account for the foregoing reasons, you may not re-register for our Services. We may block your email address and Internet protocol address to prevent further registration."
                        ),
                        TermsConditionInfo(
                            title = "Other Resources",
                            description = "Although the Mobile Application and Services may link to other resources (such as websites, mobile applications, etc.), we are not, directly or indirectly, implying any approval, association, sponsorship, endorsement, or affiliation with any linked resource, unless specifically stated herein. We are not responsible for examining or evaluating, and we do not warrant the offerings of, any businesses or individuals or the content of their resources. We do not assume any responsibility or liability for the actions, products, services, and content of any other third parties. You should carefully review the legal statements and other conditions of use of any resource which you access through a link in the Mobile Application. Your linking to any other off-site resources is at your own risk."
                        ),
                        TermsConditionInfo(
                            title = "Acceptance Of These Terms",
                            description = "<![CDATA[You acknowledge that you have read this Agreement and agree to all its terms and conditions. By accessing and using the Mobile Application and Services, you agree to be bound by this Agreement. If you do not agree to abide by the terms of this Agreement, you are not authorized to access or use the Mobile Application and Services.]]>"
                        ),
                        TermsConditionInfo(
                            title = "Contacting Us",
                            description = "If you have any questions, concerns, or complaints regarding this Agreement, we encourage you to contact us with the email below: "
                        )
                    ),
                    buttonText = "Close"
                )
            )
        }
    }

    @Nested
    inner class OnBackClicked {

        @Test
        fun `when shouldAcceptTermsParam is set to false should call navigate to settings`() {
            every { savedStateHandle.get<Boolean>("shouldAcceptTerms") } returns false

            termsConditionsViewModel = TermsConditionsViewModel(
                savedStateHandle = savedStateHandle,
                navigation = navigation,
                application = application,
                createSharedPreferences = createSharedPreferences,
                scope = scope
            )

            termsConditionsViewModel.onBackClicked()

            verify { navigation.navigateToSettings() }
            verify(exactly = 0) { navigation.finish() }
        }

        @Test
        fun `when shouldAcceptTermsParam is set to true should call finish`() {
            every { savedStateHandle.get<Boolean>("shouldAcceptTerms") } returns true

            termsConditionsViewModel = TermsConditionsViewModel(
                savedStateHandle = savedStateHandle,
                navigation = navigation,
                application = application,
                createSharedPreferences = createSharedPreferences,
                scope = scope
            )

            termsConditionsViewModel.onBackClicked()

            verify { navigation.finish() }
            verify(exactly = 0) { navigation.navigateToSettings() }
        }
    }

    @Nested
    inner class OnCloseAcceptButtonClicked {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when shouldAcceptTermsParam is set to true should call navigate to player list`() = runTest {
            Dispatchers.setMain(dispatcher)

            every { savedStateHandle.get<Boolean>("shouldAcceptTerms") } returns true

            termsConditionsViewModel = TermsConditionsViewModel(
                savedStateHandle = savedStateHandle,
                navigation = navigation,
                application = application,
                createSharedPreferences = createSharedPreferences,
                scope = scope
            )

            termsConditionsViewModel.onCloseAcceptButtonClicked()

            delay(750L)

            verify { createSharedPreferences.createShouldShowTermsAndConditionsPreference(value = false) }
            verify { navigation.navigateToPlayerList() }
            verify { navigation.navigateToOnboarding() }
        }

        @Test
        fun `when isAcknowledgeConditions is set to false should call navigate to player list`() {
            termsConditionsViewModel.onCloseAcceptButtonClicked()

            verify { navigation.navigateToSettings() }
        }
    }

    @Test
    fun `on dev email clicked should navigate to dev email`() {
        termsConditionsViewModel.onDevEmailClicked()

        verify { navigation.navigateToDevEmail(email = "nicholasrutherforddeveloper@gmail.com") }
    }
}

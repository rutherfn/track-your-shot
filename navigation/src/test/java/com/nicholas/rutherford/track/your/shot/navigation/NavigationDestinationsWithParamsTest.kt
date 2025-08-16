package com.nicholas.rutherford.track.your.shot.navigation

import com.nicholas.rutherford.track.your.shot.helper.extensions.UriEncoder
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class NavigationDestinationsWithParamsTest {

    private lateinit var navigationDestinationsWithParams: NavigationDestinationsWithParams

    @BeforeEach
    fun beforeEach() {
        navigationDestinationsWithParams = NavigationDestinationsWithParams
    }

    @Nested
    inner class BuildAuthenticationDestination {

        @Test
        fun `returns only base route when no params are provided`() {
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = null, email = null)

            Assertions.assertEquals("authenticationScreen", result)
        }

        @Test
        fun `returns only base route when empty params are provided`() {
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = "", email = "")

            Assertions.assertEquals("authenticationScreen", result)
        }

        @Test
        fun `returns route with only username when only username is provided and email is null`() {
            mockkObject(UriEncoder)

            every { UriEncoder.encode(any()) } answers { firstArg() }

            val username = "username"
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = username, email = null)

            Assertions.assertEquals("authenticationScreen?username=$username", result)

            unmockkObject(UriEncoder)
        }

        @Test
        fun `returns route with only username when only username is provided and email is empty`() {
            mockkObject(UriEncoder)

            every { UriEncoder.encode(any()) } answers { firstArg() }

            val username = "username"
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = username, email = "")

            Assertions.assertEquals("authenticationScreen?username=$username", result)

            unmockkObject(UriEncoder)
        }

        @Test
        fun `returns route with only email when only email is provided and username is null`() {
            mockkObject(UriEncoder)

            every { UriEncoder.encode(any()) } answers { firstArg() }

            val email = "emailtest@gmail.com"
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = null, email = email)

            Assertions.assertEquals("authenticationScreen?email=$email", result)

            unmockkObject(UriEncoder)
        }

        @Test
        fun `returns route with only email when only email is provided and username is empty`() {
            mockkObject(UriEncoder)

            every { UriEncoder.encode(any()) } answers { firstArg() }

            val email = "emailtest@gmail.com"
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = "", email = email)

            Assertions.assertEquals("authenticationScreen?email=$email", result)

            unmockkObject(UriEncoder)
        }

        @Test
        fun `returns route with both email and password when both are provided`() {
            mockkObject(UriEncoder)

            every { UriEncoder.encode(any()) } answers { firstArg() }

            val username = "username"
            val email = "emailtest@gmail.com"
            val result = navigationDestinationsWithParams.buildAuthenticationDestination(username = username, email = email)

            Assertions.assertEquals("authenticationScreen?username=$username&email=$email", result)

            unmockkObject(UriEncoder)
        }
    }

    @Nested
    inner class BuildTermsConditionsDestination {

        @Test
        fun `returns base route with param when passed in as true`() {
            val shouldAcceptTerms = true
            val result = navigationDestinationsWithParams.buildTermsConditionsDestination(shouldAcceptTerms = shouldAcceptTerms)

            Assertions.assertEquals("termsConditionsScreen?shouldAcceptTerms=$shouldAcceptTerms", result)
        }

        @Test
        fun `returns base route with param when passed in as false`() {
            val shouldAcceptTerms = false
            val result = navigationDestinationsWithParams.buildTermsConditionsDestination(shouldAcceptTerms = shouldAcceptTerms)

            Assertions.assertEquals("termsConditionsScreen?shouldAcceptTerms=$shouldAcceptTerms", result)
        }
    }

    @Test
    fun `BuildLogShotDestination builds full destination with all params correctly`() {
        val result = navigationDestinationsWithParams.buildLogShotDestination(
            isExistingPlayer = true,
            playerId = 42,
            shotType = 1,
            shotId = 7,
            viewCurrentExistingShot = false,
            viewCurrentPendingShot = true,
            fromShotList = true
        )

        val expected = "logShotScreen?" +
            "isExistingPlayer=true&" +
            "playerId=42&" +
            "shotType=1&" +
            "shotId=7&" +
            "viewCurrentExistingShot=false&" +
            "viewCurrentPendingShot=true&" +
            "fromShotList=true"

        Assertions.assertEquals(expected, result)
    }

    @Test
    fun `authentication with params using mocked encoder`() {
        mockkObject(UriEncoder)
        every { UriEncoder.encode(any()) } answers { firstArg() }

        val username = "username"
        val email = "emailtest@gmail.com"

        Assertions.assertEquals(
            "authenticationScreen?username=username&email=emailtest@gmail.com",
            navigationDestinationsWithParams.authenticationWithParams(username, email)
        )

        unmockkObject(UriEncoder)
    }

    @Test
    fun `create edit player with params`() {
        val firstName = "first"
        val lastName = "last"

        Assertions.assertEquals(
            navigationDestinationsWithParams.createEditPlayerWithParams(
                firstName = firstName,
                lastName = lastName
            ),
            "createEditPlayerScreen/first/last"
        )
    }

//    @Test
//    fun `terms conditions with params`() {
//        val isAcknowledgeConditions = false
//
//        Assertions.assertEquals(
//            navigationDestinationsWithParams.termsConditionsWithParams(isAcknowledgeConditions = isAcknowledgeConditions),
//            "termsConditionsScreen/false"
//        )
//    }
}

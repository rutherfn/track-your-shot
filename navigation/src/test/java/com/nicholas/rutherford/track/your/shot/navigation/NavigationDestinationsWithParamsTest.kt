package com.nicholas.rutherford.track.your.shot.navigation

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NavigationDestinationsWithParamsTest {

    private lateinit var navigationDestinationsWithParams: NavigationDestinationsWithParams

    @BeforeEach
    fun beforeEach() {
        navigationDestinationsWithParams = NavigationDestinationsWithParams
    }

    @Test
    fun `authentication with params`() {
        val username = "username"
        val email = "emailtest@gmail.com"

        Assertions.assertEquals(
            navigationDestinationsWithParams.authenticationWithParams(
                username = username,
                email = email
            ),
            "authenticationScreen/username/emailtest@gmail.com"
        )
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

    @Test
    fun `terms conditions with params`() {
        val isAcknowledgeConditions = false

        Assertions.assertEquals(
            navigationDestinationsWithParams.termsConditionsWithParams(isAcknowledgeConditions = isAcknowledgeConditions),
            "termsConditionsScreen/false"
        )
    }
}

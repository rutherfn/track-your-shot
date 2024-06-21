package com.nicholas.rutherford.track.your.shot.navigation.arguments

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NamedArgumentsTest {

    @Test
    fun constants() {
        Assertions.assertEquals(NamedArguments.EMAIL, "email")
        Assertions.assertEquals(NamedArguments.USERNAME, "username")

        Assertions.assertEquals(NamedArguments.FIRST_NAME, "firstName")
        Assertions.assertEquals(NamedArguments.LAST_NAME, "lastName")

        Assertions.assertEquals(NamedArguments.IS_EXISTING_PLAYER, "isExistingPlayer")
        Assertions.assertEquals(NamedArguments.PLAYER_ID, "playerId")

        Assertions.assertEquals(NamedArguments.CURRENT_PLAYER_SHOTS_SIZE, "currentPlayerShotsSize")
    }
}

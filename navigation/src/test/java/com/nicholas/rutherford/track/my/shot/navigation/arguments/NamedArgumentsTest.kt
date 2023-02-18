package com.nicholas.rutherford.track.my.shot.navigation.arguments

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NamedArgumentsTest {

    @Test
    fun constants() {
        Assertions.assertEquals(NamedArguments.EMAIL, "email")
        Assertions.assertEquals(NamedArguments.USERNAME, "username")
    }
}

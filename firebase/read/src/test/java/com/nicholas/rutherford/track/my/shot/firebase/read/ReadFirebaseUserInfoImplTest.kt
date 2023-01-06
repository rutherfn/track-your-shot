package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.firebase.auth.FirebaseAuth
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ReadFirebaseUserInfoImplTest {

    lateinit var readFirebaseUserInfoImpl: ReadFirebaseUserInfoImpl

    var firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)
    }

    @Nested
    inner class IsLoggedIn {

        @Test
        fun `when currentUser is set to null should set to false`() {
            every { firebaseAuth.currentUser } returns null

            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isLoggedIn)
        }

        @Test
        fun `when currentUser is not set to null should be set to true`() {
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isLoggedIn)
        }
    }
}

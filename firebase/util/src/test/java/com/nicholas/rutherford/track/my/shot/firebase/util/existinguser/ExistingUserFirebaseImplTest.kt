package com.nicholas.rutherford.track.my.shot.firebase.util.existinguser

import com.google.firebase.auth.FirebaseAuth
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExistingUserFirebaseImplTest {

    private lateinit var existingUserFirebase: ExistingUserFirebaseImpl

    private val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    @BeforeEach
    fun beforeEach() {
        existingUserFirebase = ExistingUserFirebaseImpl(firebaseAuth = firebaseAuth)
    }

    @Test
    fun `logout should call firebaseAuth signOut`() {
        existingUserFirebase.logOut()

        verify { firebaseAuth.signOut() }
    }
}

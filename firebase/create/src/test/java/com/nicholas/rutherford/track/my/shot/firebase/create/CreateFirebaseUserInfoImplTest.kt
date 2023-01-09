package com.nicholas.rutherford.track.my.shot.firebase.create

import android.app.Activity
import com.google.android.gms.tasks.*
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.nicholas.rutherford.track.my.shot.data.test.account.info.TestCreateAccountResponse
import io.mockk.MockK
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.lang.Exception
import java.util.concurrent.Executor

class CreateFirebaseUserInfoImplTest {

    lateinit var createFirebaseUserInfoImpl: CreateFirebaseUserInfoImpl

    val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)

    val createAccountResponse = TestCreateAccountResponse().create()

    val testEmail = "testemail@yahoo.com"
    val testPassword = "passwordTest112"

    private lateinit var successTask: Task<AuthResult>

    @BeforeEach
    fun beforeEach() {
        createFirebaseUserInfoImpl = CreateFirebaseUserInfoImpl(firebaseAuth = firebaseAuth)
    }

    @Nested
    inner class AttemptToCreateAccountResponseFlow {

        @Test
        fun `when add on complete listener is executed should set flow to valid create account response flow`() {
            mockkStatic(Tasks::class)
            every { firebaseAuth.createUserWithEmailAndPassword(testEmail, testPassword) } returns successTask
        }

        @Test
        fun `when add on failure listener is executed should set flow to valid create account response flow`() {

        }
    }
}
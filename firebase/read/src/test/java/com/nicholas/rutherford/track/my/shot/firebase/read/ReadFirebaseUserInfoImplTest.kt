package com.nicholas.rutherford.track.my.shot.firebase.read

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nicholas.rutherford.track.my.shot.data.firebase.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.my.shot.data.test.firebase.TestAccountInfoRealTimeResponse
import com.nicholas.rutherford.track.my.shot.helper.constants.Constants
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Date

class ReadFirebaseUserInfoImplTest {

    lateinit var readFirebaseUserInfoImpl: ReadFirebaseUserInfoImpl

    var firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    var firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)

    val accountInfoRealtimeResponse = TestAccountInfoRealTimeResponse().create()
    val currentDate = Date()

    @BeforeEach
    fun beforeEach() {
        readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class GetLoggedInAccountEmail {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to null`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser email is set to null should set to null`() = runTest {
            every { firebaseAuth.currentUser!!.email } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser and email is not set to null should set value to returned email`() = runTest {
            every { firebaseAuth.currentUser!!.email } returns accountInfoRealtimeResponse.email
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(accountInfoRealtimeResponse.email, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }
    }

    @Nested
    inner class GetAccountInfoListFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onCancelled is called should return null`() = runTest {
            val mockDatabaseError = mockk<DatabaseError>()
            val slot = slot<ValueEventListener>()

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onCancelled(mockDatabaseError)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoListFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called with snapshot exists return back as false should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockDataSnapshotList = listOf(mockDataSnapshot)
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns false
            every { mockDataSnapshot.childrenCount } returns mockDataSnapshotList.size.toLong()
            every { mockDataSnapshot.getValue(AccountInfoRealtimeResponse::class.java) } returns accountInfoRealtimeResponse
            every { mockDataSnapshot.children } returns mockDataSnapshotList

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoListFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called with snapshot exists return back as true with snapshot child data should return info`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockDataSnapshotList = listOf(mockDataSnapshot)
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.childrenCount } returns mockDataSnapshotList.size.toLong()
            every { mockDataSnapshot.getValue(AccountInfoRealtimeResponse::class.java) } returns accountInfoRealtimeResponse
            every { mockDataSnapshot.children } returns mockDataSnapshotList

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(listOf(accountInfoRealtimeResponse), readFirebaseUserInfoImpl.getAccountInfoListFlow().first())
        }
    }

    @Nested
    inner class GetLastUpdatedDateFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onCancelled is called should return null`() = runTest {
            val mockDatabaseError = mockk<DatabaseError>()
            val slot = slot<ValueEventListener>()

            every {
                firebaseDatabase.getReference(Constants.CONTENT_LAST_UPDATED_PATH)
                    .child(Constants.LAST_UPDATED)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onCancelled(mockDatabaseError)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLastUpdatedDateFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called but snapshot does not exist should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns false

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.CONTENT_LAST_UPDATED_PATH)
                    .child(Constants.LAST_UPDATED)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLastUpdatedDateFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called, snapshot exists, and value returns null should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.getValue(Long::class.java) } returns null

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.CONTENT_LAST_UPDATED_PATH)
                    .child(Constants.LAST_UPDATED)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLastUpdatedDateFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called, snapshot exists, and value returns date should return valid date`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.getValue(Long::class.java) } returns currentDate.time

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.CONTENT_LAST_UPDATED_PATH)
                    .child(Constants.LAST_UPDATED)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(currentDate, readFirebaseUserInfoImpl.getLastUpdatedDateFlow().first())
        }
    }

    @Nested
    inner class GetAccountInfoFlowByEmail {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onCancelled is called should return null`() = runTest {
            val mockDatabaseError = mockk<DatabaseError>()
            val slot = slot<ValueEventListener>()

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .orderByChild(Constants.EMAIL)
                    .equalTo(accountInfoRealtimeResponse.email)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onCancelled(mockDatabaseError)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called but the count is 0 should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockDataSnapshotList = listOf(mockDataSnapshot)
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.childrenCount } returns mockDataSnapshotList.size.toLong()
            every { mockDataSnapshot.getValue(AccountInfoRealtimeResponse::class.java) } returns null
            every { mockDataSnapshot.children } returns mockDataSnapshotList

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .orderByChild(Constants.EMAIL)
                    .equalTo(accountInfoRealtimeResponse.email)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called but the count is greater then 1 should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockDataSnapshotList = listOf(mockDataSnapshot, mockDataSnapshot)
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.childrenCount } returns mockDataSnapshotList.size.toLong()
            every { mockDataSnapshot.getValue(AccountInfoRealtimeResponse::class.java) } returns accountInfoRealtimeResponse
            every { mockDataSnapshot.children } returns mockDataSnapshotList

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .orderByChild(Constants.EMAIL)
                    .equalTo(accountInfoRealtimeResponse.email)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called with snapshot exists return back as false should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockDataSnapshotList = listOf(mockDataSnapshot)
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns false
            every { mockDataSnapshot.childrenCount } returns mockDataSnapshotList.size.toLong()
            every { mockDataSnapshot.getValue(AccountInfoRealtimeResponse::class.java) } returns accountInfoRealtimeResponse
            every { mockDataSnapshot.children } returns mockDataSnapshotList

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .orderByChild(Constants.EMAIL)
                    .equalTo(accountInfoRealtimeResponse.email)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when onDataChange is called with valid conditions should return accountInfoRealTimeResponse`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockDataSnapshotList = listOf(mockDataSnapshot)
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.childrenCount } returns mockDataSnapshotList.size.toLong()
            every { mockDataSnapshot.getValue(AccountInfoRealtimeResponse::class.java) } returns accountInfoRealtimeResponse
            every { mockDataSnapshot.children } returns mockDataSnapshotList

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .orderByChild(Constants.EMAIL)
                    .equalTo(accountInfoRealtimeResponse.email)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(accountInfoRealtimeResponse, readFirebaseUserInfoImpl.getAccountInfoFlowByEmail(accountInfoRealtimeResponse.email).first())
        }
    }

    @Nested
    inner class IsEmailVerifiedFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not null but complete listener is not successful should be set to false`() = runTest {
            val mockTaskReloadResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            every { mockTaskReloadResult.isSuccessful } returns false

            mockkStatic(Tasks::class)

            every { firebaseAuth.currentUser!!.reload().addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskReloadResult)
                mockTaskReloadResult
            }

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not null but complete listener is successful with email verified set to false should be set to false`() = runTest {
            val mockTaskReloadResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            every { firebaseAuth.currentUser!!.isEmailVerified } returns false
            every { mockTaskReloadResult.isSuccessful } returns true

            mockkStatic(Tasks::class)

            every { firebaseAuth.currentUser!!.reload().addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskReloadResult)
                mockTaskReloadResult
            }

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not null but complete listener is successful with email verified set to true should be set to true`() = runTest {
            val mockTaskReloadResult = mockk<Task<Void>>()
            val slot = slot<OnCompleteListener<Void>>()

            every { firebaseAuth.currentUser!!.isEmailVerified } returns true
            every { mockTaskReloadResult.isSuccessful } returns true

            mockkStatic(Tasks::class)

            every { firebaseAuth.currentUser!!.reload().addOnCompleteListener(capture(slot)) } answers {
                slot.captured.onComplete(mockTaskReloadResult)
                mockTaskReloadResult
            }

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
        }
    }

    @Nested
    inner class IsLoggedInFlow {

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isLoggedInFlow().first())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        fun `when currentUser is not set to null should be set to true`() = runTest {
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isLoggedInFlow().first())
        }
    }
}

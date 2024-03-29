package com.nicholas.rutherford.track.your.shot.firebase.core.read

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nicholas.rutherford.track.your.shot.firebase.realtime.AccountInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.PlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestAccountInfoRealTimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeResponse
import com.nicholas.rutherford.track.your.shot.firebase.realtime.TestPlayerInfoRealtimeWithKeyResponse
import com.nicholas.rutherford.track.your.shot.helper.constants.Constants
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
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

    val firebaseAccountKey = "-kTE1212D"

    @BeforeEach
    fun beforeEach() {
        readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)
    }

    @Nested
    inner class GetLoggedInAccountEmail {

        @Test
        fun `when currentUser is set to null should set to null`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }

        @Test
        fun `when currentUser email is set to null should set to null`() = runTest {
            every { firebaseAuth.currentUser!!.email } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }

        @Test
        fun `when currentUser and email is not set to null should set value to returned email`() = runTest {
            every { firebaseAuth.currentUser!!.email } returns accountInfoRealtimeResponse.email
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(accountInfoRealtimeResponse.email, readFirebaseUserInfoImpl.getLoggedInAccountEmail().first())
        }
    }

    @Nested
    inner class GetAccountInfoListFlow {

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
    inner class GetAccountInfoKeyByEmail {

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

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoKeyFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @Test
        fun `when onDataChange is called but snapshot does not exist should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns false

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

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoKeyFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @Test
        fun `when onDataChange is called, snapshot exists, and children key returns empty should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.children } returns emptyList<DataSnapshot>()

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

            Assertions.assertEquals(null, readFirebaseUserInfoImpl.getAccountInfoKeyFlowByEmail(accountInfoRealtimeResponse.email).first())
        }

        @Test
        fun `when onDataChange is called, snapshot exists, and children key returns info should return null`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val mockChildDataSnapshot = mockk<DataSnapshot>()

            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.children } returns listOf(mockChildDataSnapshot)
            every { mockChildDataSnapshot.key } returns firebaseAccountKey

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

            Assertions.assertEquals(firebaseAccountKey, readFirebaseUserInfoImpl.getAccountInfoKeyFlowByEmail(accountInfoRealtimeResponse.email).first())
        }
    }

    @Nested
    inner class GetAccountInfoFlowByEmail {

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
    inner class GetPlayerInfoList {
        private val playerInfoRealtimeWithKeyResponseEmptyList: List<PlayerInfoRealtimeWithKeyResponse> = emptyList()

        @Test
        fun `when onCancelled is called should return empty list`() = runTest {
            val mockDatabaseError = mockk<DatabaseError>()
            val slot = slot<ValueEventListener>()

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onCancelled(mockDatabaseError)
            }

            Assertions.assertEquals(playerInfoRealtimeWithKeyResponseEmptyList, readFirebaseUserInfoImpl.getPlayerInfoList(accountKey = firebaseAccountKey).first())
        }

        @Test
        fun `when on data change is called but snapshot does not exist should return empty list`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns false

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(playerInfoRealtimeWithKeyResponseEmptyList, readFirebaseUserInfoImpl.getPlayerInfoList(accountKey = firebaseAccountKey).first())
        }

        @Test
        fun `when snapshot exists but children count is zero should return empty list`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.childrenCount } returns 0

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(playerInfoRealtimeWithKeyResponseEmptyList, readFirebaseUserInfoImpl.getPlayerInfoList(accountKey = firebaseAccountKey).first())
        }

        @Test
        fun `when snapshot exists and has children should return list of player info`() = runTest {
            val mockDataSnapshot = mockk<DataSnapshot>()
            val slot = slot<ValueEventListener>()

            val playerInfoRealtimeWithKeyResponseList = listOf(
                TestPlayerInfoRealtimeWithKeyResponse().create(),
                TestPlayerInfoRealtimeWithKeyResponse().create().copy(playerFirebaseKey = "-NgBwuq_5kti8ChLDuhc", playerInfo = TestPlayerInfoRealtimeResponse().create().copy(firstName = "firstName2"))
            )

            every { mockDataSnapshot.exists() } returns true
            every { mockDataSnapshot.childrenCount } returns playerInfoRealtimeWithKeyResponseList.size.toLong()

            every { mockDataSnapshot.children } returns playerInfoRealtimeWithKeyResponseList.map { playerInfoRealtimeWithKey ->
                val mockChildSnapshot = mockk<DataSnapshot>()
                every { mockChildSnapshot.key } returns playerInfoRealtimeWithKey.playerFirebaseKey
                every { mockChildSnapshot.getValue(PlayerInfoRealtimeResponse::class.java) } returns playerInfoRealtimeWithKey.playerInfo
                mockChildSnapshot
            }

            mockkStatic(DataSnapshot::class)

            every {
                firebaseDatabase.getReference(Constants.USERS)
                    .child(Constants.ACCOUNT_INFO)
                    .child(firebaseAccountKey)
                    .child(Constants.PLAYERS)
                    .addListenerForSingleValueEvent(capture(slot))
            } answers {
                slot.captured.onDataChange(mockDataSnapshot)
            }

            Assertions.assertEquals(playerInfoRealtimeWithKeyResponseList, readFirebaseUserInfoImpl.getPlayerInfoList(accountKey = firebaseAccountKey).first())
        }
    }

    @Nested
    inner class IsEmailVerifiedFlow {

        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isEmailVerifiedFlow().first())
        }

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

        @Test
        fun `when currentUser is set to null should set to false`() = runTest {
            every { firebaseAuth.currentUser } returns null
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(false, readFirebaseUserInfoImpl.isLoggedInFlow().first())
        }

        @Test
        fun `when currentUser is not set to null should be set to true`() = runTest {
            readFirebaseUserInfoImpl = ReadFirebaseUserInfoImpl(firebaseAuth = firebaseAuth, firebaseDatabase = firebaseDatabase)

            Assertions.assertEquals(true, readFirebaseUserInfoImpl.isLoggedInFlow().first())
        }
    }
}

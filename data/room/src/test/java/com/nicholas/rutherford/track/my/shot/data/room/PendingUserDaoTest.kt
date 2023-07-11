package com.nicholas.rutherford.track.my.shot.data.room

import android.app.Application
import androidx.room.Room
import com.nicholas.rutherford.track.my.shot.data.room.dao.PendingUserDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.test.room.TestPendingUser
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PendingUserDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var pendingUserDao: PendingUserDao

    @Mock
    private lateinit var application: Application

    private val pendingUser = TestPendingUser().create()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val context = mock(Application::class.java)
        `when`(application.applicationContext).thenReturn(context)

        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()

        pendingUserDao = appDatabase.pendingUserDao()
    }

    @AfterEach
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun `insert should update database with pending user when attempting to get pending user`() {
        pendingUserDao.insert(pendingUser)

        val retrievedPendingUser = pendingUserDao.getPendingUser()

        assertThat(pendingUser, equalTo(retrievedPendingUser))
    }
}

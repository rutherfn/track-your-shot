package com.nicholas.rutherford.track.my.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.PendingUserDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.test.room.TestPendingUser
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PendingUserDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var pendingUserDao: PendingUserDao

    private val pendingUser = TestPendingUser().create()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        pendingUserDao = appDatabase.pendingUserDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun update() {
        pendingUserDao.insert(pendingUser = pendingUser)

        assertThat(pendingUser, equalTo(pendingUserDao.getPendingUser()))

        pendingUserDao.update(pendingUser = pendingUser.copy(unverifiedUsername = "Username11"))

        assertThat(pendingUserDao.getPendingUser(), equalTo(pendingUser.copy(unverifiedUsername = "Username11")))
    }

    @Test
    fun delete() {
        pendingUserDao.insert(pendingUser = pendingUser)

        assertThat(pendingUser, equalTo(pendingUserDao.getPendingUser()))

        pendingUserDao.delete(pendingUser = pendingUser)

        assertThat(pendingUserDao.getPendingUser(), equalTo(null))
    }

    @Test
    fun getPendingUser() {
        pendingUserDao.insert(pendingUser = pendingUser)

        val retrievedPendingUser = pendingUserDao.getPendingUser()

        assertThat(pendingUser, equalTo(retrievedPendingUser))
    }
}

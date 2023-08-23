package com.nicholas.rutherford.track.my.shot.data.room.repositorys

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.room.repository.ActiveUserRepositoryImpl
import com.nicholas.rutherford.track.my.shot.data.test.room.TestActiveUser
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActiveUserRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var activeUserDao: ActiveUserDao

    private val activeUser = TestActiveUser().create()

    private lateinit var activeUserRepositoryImpl: ActiveUserRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        activeUserDao = appDatabase.activeUserDao()
        activeUserRepositoryImpl = ActiveUserRepositoryImpl(activeUserDao = activeUserDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createActiveUser() = runBlocking {
        activeUserRepositoryImpl.createActiveUser(activeUser = activeUser)

        assertThat(activeUserRepositoryImpl.fetchActiveUser(), equalTo(activeUser))
    }

    @Test
    fun updateActiveUser() = runBlocking {
        activeUserRepositoryImpl.createActiveUser(activeUser = activeUser)

        assertThat(activeUserRepositoryImpl.fetchActiveUser(), equalTo(activeUser))

        activeUserRepositoryImpl.updateActiveUser(activeUser = activeUser.copy(username = "test"))

        assertThat(activeUserRepositoryImpl.fetchActiveUser(), equalTo(activeUser.copy(username = "test")))
    }

    @Test
    fun deleteActiveUser() = runBlocking {
        activeUserRepositoryImpl.createActiveUser(activeUser = activeUser)

        assertThat(activeUserRepositoryImpl.fetchActiveUser(), equalTo(activeUser))

        activeUserRepositoryImpl.deleteActiveUser()

        assertThat(activeUserRepositoryImpl.fetchActiveUser(), equalTo(null))
    }

    @Test
    fun fetchActiveUser() = runBlocking {
        activeUserRepositoryImpl.createActiveUser(activeUser = activeUser)

        assertThat(activeUserRepositoryImpl.fetchActiveUser(), equalTo(activeUser))
    }
}

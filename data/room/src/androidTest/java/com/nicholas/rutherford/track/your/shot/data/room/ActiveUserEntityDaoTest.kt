package com.nicholas.rutherford.track.your.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.your.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.your.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.your.shot.data.test.room.TestActiveUserEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActiveUserEntityDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var activeUserDao: ActiveUserDao

    private val activeUserEntity = TestActiveUserEntity().create()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        activeUserDao = appDatabase.activeUserDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        activeUserDao.insert(activeUserEntity = activeUserEntity)

        assertThat(activeUserEntity, equalTo(activeUserDao.getActiveUser()))
    }

    @Test
    fun update() = runBlocking {
        activeUserDao.insert(activeUserEntity = activeUserEntity)

        assertThat(activeUserEntity, equalTo(activeUserDao.getActiveUser()))

        activeUserDao.update(activeUserEntity = activeUserEntity.copy(username = "Username11"))

        assertThat(activeUserDao.getActiveUser(), equalTo(activeUserEntity.copy(username = "Username11")))
    }

    @Test
    fun delete() = runBlocking {
        activeUserDao.insert(activeUserEntity = activeUserEntity)

        activeUserDao.delete()

        assertThat(activeUserDao.getActiveUser(), equalTo(null))
    }

    @Test
    fun getActiveUser() = runBlocking {
        activeUserDao.insert(activeUserEntity = activeUserEntity)

        val retrievedPActiveUser = activeUserDao.getActiveUser()

        assertThat(activeUserEntity, equalTo(retrievedPActiveUser))
    }
}

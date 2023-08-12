package com.nicholas.rutherford.track.my.shot.data.room

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.test.room.TestUserEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserEntityDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var userDao: UserDao

    private val userEntity = TestUserEntity().create()

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = appDatabase.userDao()
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun insert() = runBlocking {
        userDao.insert(userEntity)

        MatcherAssert.assertThat(listOf(userEntity), equalTo(userDao.getAllUsers()))
    }

    @Test
    fun insertUsers() = runBlocking {
        val users = listOf(
            userEntity,
            userEntity.copy(id = 2, username = "test11", email = "email@test.com"),
            userEntity.copy(id = 3, username = "test112", email = "email2@test.com")
        )
        userDao.insertUsers(users = users)

        MatcherAssert.assertThat(users, equalTo(userDao.getAllUsers()))
    }

    @Test
    fun update() = runBlocking {
        userDao.insert(userEntity)

        MatcherAssert.assertThat(listOf(userEntity), equalTo(userDao.getAllUsers()))

        userDao.update(userEntity.copy(username = "Username11"))

        MatcherAssert.assertThat(listOf(userEntity.copy(username = "Username11")), equalTo(userDao.getAllUsers()))
    }

    @Test
    fun delete() = runBlocking {
        val secondUserEntity = userEntity.copy(id = 2, username = "test11", email = "email@test.com")

        userDao.insert(userEntity)
        userDao.insert(secondUserEntity)

        MatcherAssert.assertThat(listOf(userEntity, secondUserEntity), equalTo(userDao.getAllUsers()))

        userDao.delete(userEntity)

        MatcherAssert.assertThat(listOf(secondUserEntity), equalTo(userDao.getAllUsers()))
    }

    @Test
    fun deleteAll() = runBlocking {
        val secondUserEntity = userEntity.copy(id = 2, username = "test11", email = "email@test.com")

        userDao.insert(userEntity)
        userDao.insert(secondUserEntity)

        MatcherAssert.assertThat(listOf(userEntity, secondUserEntity), equalTo(userDao.getAllUsers()))

        userDao.deleteAllUsers()

        MatcherAssert.assertThat(emptyList(), equalTo(userDao.getAllUsers()))
    }

    @Test
    fun getAllUsers() = runBlocking {
        userDao.insert(userEntity)

        val retrievedAllUsers = userDao.getAllUsers()

        MatcherAssert.assertThat(listOf(userEntity), equalTo(retrievedAllUsers))
    }
}

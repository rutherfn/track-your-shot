package com.nicholas.rutherford.track.my.shot.data.room.repositorys

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholas.rutherford.track.my.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.my.shot.data.room.database.AppDatabase
import com.nicholas.rutherford.track.my.shot.data.room.repository.UserRepositoryImpl
import com.nicholas.rutherford.track.my.shot.data.test.room.TestUser
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRepositoryImplTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var userDao: UserDao

    private val user = TestUser().create()

    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @Before
    fun setup() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        userDao = appDatabase.userDao()

        userRepositoryImpl = UserRepositoryImpl(userDao = userDao)
    }

    @After
    fun teardown() {
        appDatabase.close()
    }

    @Test
    fun createUser() = runBlocking {
        userRepositoryImpl.createUser(user = user)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(user)))
    }

    @Test
    fun createUsers() = runBlocking {
        val userList = listOf(user, user.copy(id = 2, username = "test"))

        userRepositoryImpl.createUsers(userList = userList)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(userList))
    }

    @Test
    fun updateUser() = runBlocking {
        userRepositoryImpl.createUser(user = user)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(user)))

        userRepositoryImpl.updateUser(user = user.copy(username = "test"))

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(user.copy(username = "test"))))
    }

    @Test
    fun deleteUser() = runBlocking {
        val secondUser = user.copy(id = 2, username = "test")

        userRepositoryImpl.createUser(user = user)
        userRepositoryImpl.createUser(user = secondUser)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(user, secondUser)))

        userRepositoryImpl.deleteUser(user = user)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(secondUser)))
    }

    @Test
    fun deleteAllUsers() = runBlocking {
        val secondUser = user.copy(id = 2, username = "test")

        userRepositoryImpl.createUser(user = user)
        userRepositoryImpl.createUser(user = secondUser)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(user, secondUser)))

        userRepositoryImpl.deleteAllUsers()

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(emptyList()))
    }

    @Test
    fun fetchUserByEmail() = runBlocking {
        val secondUser = user.copy(id = 2, email = "emailtest@gmail.com", username = "test")

        userRepositoryImpl.createUser(user = user)
        userRepositoryImpl.createUser(user = secondUser)

        assertThat(userRepositoryImpl.fetchUserByEmail(email = secondUser.email), CoreMatchers.equalTo(secondUser))
    }

    @Test
    fun fetchAllUsers() = runBlocking {
        userRepositoryImpl.createUser(user = user)

        assertThat(userRepositoryImpl.fetchAllUsers(), CoreMatchers.equalTo(listOf(user)))
    }
}

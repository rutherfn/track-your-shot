package com.nicholas.rutherford.track.my.shot.data.room.repository

import com.nicholas.rutherford.track.my.shot.data.room.dao.UserDao
import com.nicholas.rutherford.track.my.shot.data.room.entities.toUser
import com.nicholas.rutherford.track.my.shot.data.room.response.User
import com.nicholas.rutherford.track.my.shot.data.room.response.toUserEntity

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun createUser(user: User) = userDao.insert(userEntity = user.toUserEntity())

    override suspend fun createUsers(userList: List<User>) = userDao.insertUsers(users = userList.map { it.toUserEntity() })

    override suspend fun updateUser(user: User) = userDao.update(userEntity = user.toUserEntity())

    override suspend fun deleteAllUsers() = userDao.deleteAllUsers()

    override suspend fun deleteUser(user: User) = userDao.delete(userEntity = user.toUserEntity())

    override suspend fun fetchUserByEmail(email: String): User? =
        userDao.getUserByEmail(email = email)?.toUser()

    override suspend fun fetchAllUsers(): List<User> {
        return userDao.getAllUsers().map { it.toUser() }
    }
}

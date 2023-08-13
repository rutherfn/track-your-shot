package com.nicholas.rutherford.track.my.shot.data.room.repository

import com.nicholas.rutherford.track.my.shot.data.room.response.User

interface UserRepository {
    suspend fun createUser(user: User)

    suspend fun createUsers(userList: List<User>)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun deleteAllUsers()
    suspend fun fetchAllUsers(): List<User>
}

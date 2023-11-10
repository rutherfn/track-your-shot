package com.nicholas.rutherford.track.your.shot.data.room.repository

import com.nicholas.rutherford.track.your.shot.data.room.dao.ActiveUserDao
import com.nicholas.rutherford.track.your.shot.data.room.entities.toActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.ActiveUser
import com.nicholas.rutherford.track.your.shot.data.room.response.toActiveUserEntity

class ActiveUserRepositoryImpl(private val activeUserDao: ActiveUserDao) : ActiveUserRepository {

    override suspend fun createActiveUser(activeUser: ActiveUser) = activeUserDao.insert(activeUserEntity = activeUser.toActiveUserEntity())

    override suspend fun updateActiveUser(activeUser: ActiveUser) = activeUserDao.update(activeUserEntity = activeUser.toActiveUserEntity())

    override suspend fun deleteActiveUser() = activeUserDao.delete()

    override suspend fun fetchActiveUser(): ActiveUser? = activeUserDao.getActiveUser()?.toActiveUser()
}

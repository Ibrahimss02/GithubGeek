package com.ibrahimss.githubgeek.data.local

import com.ibrahimss.githubgeek.data.IUserDataSource
import com.ibrahimss.githubgeek.data.model.UserResponse

class UserDataSource internal constructor(
    private val userDao: UserDao
) : IUserDataSource {
    override fun getAllUsers() = userDao.getAllUsers()
    override fun getUser(username: String) = userDao.getUser(username)
    override fun insertUser(user: UserResponse) = userDao.insertUser(user)
    override fun deleteUser(user: UserResponse) = userDao.deleteUser(user)
    override fun getUserFavoriteState(userId: Int) = userDao.isUserFavorite(userId)
}
package com.ibrahimss.githubgeek.data

import com.ibrahimss.githubgeek.data.model.UserResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DefaultUserRepository constructor(
    private val userDataSource: IUserDataSource
) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun getAllUser() = userDataSource.getAllUsers()

    fun getUser(username: String) = userDataSource.getUser(username)

    fun insertUser(user: UserResponse) {
        executorService.execute {
            userDataSource.insertUser(user)
        }
    }

    fun delete(user: UserResponse) {
        executorService.execute {
            userDataSource.deleteUser(user)
        }
    }

    fun getUserFavoriteState(userId: Int) = userDataSource.getUserFavoriteState(userId)
}
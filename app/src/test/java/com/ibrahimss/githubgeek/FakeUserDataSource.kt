package com.ibrahimss.githubgeek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ibrahimss.githubgeek.data.IUserDataSource
import com.ibrahimss.githubgeek.data.model.UserResponse

/**
 * Data source palsu
 */
class FakeUserDataSource(private var users: MutableList<UserResponse>? = mutableListOf()): IUserDataSource {
    override fun getAllUsers(): LiveData<List<UserResponse>> = MutableLiveData(users)

    override fun getUser(username: String): LiveData<UserResponse> {
        return MutableLiveData(users?.firstOrNull { it.username == username })
    }

    override fun insertUser(user: UserResponse) {
        users?.add(user)
    }

    override fun deleteUser(user: UserResponse) {
        users?.remove(user)
    }

    override fun getUserFavoriteState(userId: Int): Boolean {
        return users?.any { it.id == userId } ?: false
    }
}
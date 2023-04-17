package com.ibrahimss.githubgeek.data

import androidx.lifecycle.LiveData
import com.ibrahimss.githubgeek.data.model.UserResponse

/**
 * Abstraksi dari data source, agar dapat dipakai untuk keperluan testing (membuat data source fake).
 */
interface IUserDataSource {
    fun getAllUsers(): LiveData<List<UserResponse>>
    fun getUser(username: String): LiveData<UserResponse>
    fun insertUser(user: UserResponse)
    fun deleteUser(user: UserResponse)
    fun getUserFavoriteState(userId: Int): Boolean
}
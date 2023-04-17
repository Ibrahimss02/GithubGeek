package com.ibrahimss.githubgeek.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.ibrahimss.githubgeek.data.model.UserResponse

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: UserResponse)

    @Delete
    fun deleteUser(user: UserResponse)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<UserResponse>>

    @Query("SELECT * FROM user_table WHERE username = :username")
    fun getUser(username: String): LiveData<UserResponse>

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE id = :id)")
    fun isUserFavorite(id : Int) : Boolean
}
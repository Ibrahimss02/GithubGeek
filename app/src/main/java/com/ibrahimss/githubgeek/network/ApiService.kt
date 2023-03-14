package com.ibrahimss.githubgeek.network

import com.ibrahimss.githubgeek.model.SearchResponse
import com.ibrahimss.githubgeek.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): Call<List<UserResponse>>

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Call<UserResponse>

    @GET("search/users")
    suspend fun searchUsers(@Query("q") keyword: String): Call<SearchResponse>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Call<List<UserResponse>>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Call<List<UserResponse>>
}
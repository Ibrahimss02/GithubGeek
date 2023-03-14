package com.ibrahimss.githubgeek.model

import com.squareup.moshi.Json

data class UserResponse(
    val id: Int,

    @Json(name = "login")
    val username: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "url")
    val url: String,

    @Json(name = "html_url")
    val htmlUrl: String,
)
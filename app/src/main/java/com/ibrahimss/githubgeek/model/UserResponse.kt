package com.ibrahimss.githubgeek.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class UserResponse(
    val id: Int,

    @Json(name = "login")
    val username: String,

    @Json(name = "name")
    val name: String? = "",

    @Json(name = "bio")
    val bio: String? = "",

    @Json(name = "following")
    val following: Int = 0,

    @Json(name = "followers")
    val followers: Int = 0,

    @Json(name = "public_repos")
    val publicRepos: Int = 0,

    @Json(name = "blog")
    val blog: String ?= "",

    @Json(name = "location")
    val location: String? = "",

    @Json(name = "company")
    val company: String? = "",

    @Json(name = "avatar_url")
    val avatarUrl: String,

    @Json(name = "url")
    val url: String,

    @Json(name = "html_url")
    val htmlUrl: String,

    @Json(name = "following_url")
    val followingUrl: String = "",

    @Json(name = "followers_url")
    val followersUrl: String = "",
): Parcelable
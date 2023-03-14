package com.ibrahimss.githubgeek.model

import com.squareup.moshi.Json

data class SearchResponse(
    @Json(name = "total_count")
    val totalCount: Int,

    val items: List<UserResponse>
)

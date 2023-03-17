package com.ibrahimss.githubgeek.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.github.com/"

val authInterceptor = Interceptor { chain ->
    val req = chain.request()
    val requestHeaders = req.newBuilder()
        .addHeader("Authorization", "token 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")
        .build()
    chain.proceed(requestHeaders)
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = okhttp3.OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

object GithubApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
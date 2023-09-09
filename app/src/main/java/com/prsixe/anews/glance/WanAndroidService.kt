package com.prsixe.anews.glance

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val WANANDROID = "https://www.wanandroid.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(WANANDROID)
    .build()

object WanAndroidApi{
    val wanAndroidService:WanAndroidService by lazy {
        retrofit.create(WanAndroidService::class.java)

    }
}

interface WanAndroidService {
    @GET("article/list/0/json")
    suspend fun getArticles(): Response
}
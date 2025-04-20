package com.sree.imagelistapplication.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RestApisService {
    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String, // Pexels API Key
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1
    ): Response<PexelResponse>
}
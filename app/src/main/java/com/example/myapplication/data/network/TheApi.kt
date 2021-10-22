package com.example.myapplication.data.network

import com.example.myapplication.data.network.model.ImageResponse
import com.example.myapplication.data.network.model.Order
import retrofit2.http.GET
import retrofit2.http.Query

interface TheApi {

    @GET("images/search")
    suspend fun fetchImage(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("order") order: Order
    ): List<ImageResponse>

    @GET("images/search")
    suspend fun fetchImage(
        @Query("limit") limit: Int
    ): List<ImageResponse>
}


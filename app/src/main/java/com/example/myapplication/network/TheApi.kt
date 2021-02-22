package com.example.myapplication.network

import com.example.myapplication.model.ImageData
import retrofit2.http.GET
import retrofit2.http.Query

interface TheApi {

    @GET("images/search")
    suspend fun fetchImage(
        @Query("limit") limit: Int,
        @Query("page") page: Int,
        @Query("order") order: Order
    ): List<ImageData>

    @GET("images/search")
    suspend fun fetchImage(
        @Query("limit") limit: Int
    ): List<ImageData>
}


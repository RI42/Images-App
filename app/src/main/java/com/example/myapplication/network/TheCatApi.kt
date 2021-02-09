package com.example.myapplication.network

import com.example.myapplication.model.ImageData
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApi {

    companion object {
        const val BASE_URL = "https://api.thecatapi.com/v1/"
    }

    @GET("images/search")
    suspend fun fetchImage(
        @Query("limit") limit: Int,
        @Query("order") order: Order
    ): List<ImageData>
}


package com.example.myapplication.domain

import androidx.paging.PagingData
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImagesPagingFlow(pageSize: Int): Flow<PagingData<ImageEntity>>
    suspend fun setState(id: String, state: ImageState)
}
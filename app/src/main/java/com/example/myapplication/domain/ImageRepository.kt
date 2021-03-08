package com.example.myapplication.domain

import androidx.paging.PagingData
import com.example.myapplication.model.FilterInfo
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import com.example.myapplication.model.SourceType
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImagesPagingFlow(pageSize: Int, sourceType: SourceType): Flow<PagingData<ImageEntity>>
    suspend fun setState(image: ImageEntity, state: ImageState)

    fun filteredImagesFlow(pageSize: Int, info: FilterInfo): Flow<PagingData<ImageEntity>>
}
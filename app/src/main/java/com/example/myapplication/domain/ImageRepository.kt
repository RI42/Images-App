package com.example.myapplication.domain

import androidx.paging.PagingData
import com.example.myapplication.domain.model.FilterInfo
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.ImageState
import com.example.myapplication.domain.model.SourceType
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImagesPagingFlow(sourceType: SourceType): Flow<PagingData<Image>>
    suspend fun setState(image: Image, state: ImageState)

    fun filteredImagesFlow(info: FilterInfo): Flow<PagingData<Image>>
}
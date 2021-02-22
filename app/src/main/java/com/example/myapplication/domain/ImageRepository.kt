package com.example.myapplication.domain

import androidx.paging.PagingData
import com.example.myapplication.model.ImageEntity
import kotlinx.coroutines.flow.Flow


interface ImageRepository {

    fun getImagesPagingFlow(pageSize: Int): Flow<PagingData<ImageEntity>>
}
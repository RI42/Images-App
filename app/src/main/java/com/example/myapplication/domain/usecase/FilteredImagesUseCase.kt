package com.example.myapplication.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.consts.Consts
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.model.FilterInfo
import com.example.myapplication.model.ImageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FilteredImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(info: FilterInfo): Flow<PagingData<ImageEntity>> =
        imageRepository.filteredImagesFlow(Consts.PAGE_SIZE, info)
}
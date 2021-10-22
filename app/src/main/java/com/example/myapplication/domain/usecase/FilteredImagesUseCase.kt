package com.example.myapplication.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.consts.Consts
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.model.FilterInfo
import com.example.myapplication.domain.model.Image
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FilteredImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(info: FilterInfo): Flow<PagingData<Image>> =
        imageRepository.filteredImagesFlow(info)
}
package com.example.myapplication.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.filter.SourceType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(sourceType: SourceType): Flow<PagingData<Image>> =
        imageRepository.getImagesPagingFlow(sourceType)

}
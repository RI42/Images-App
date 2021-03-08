package com.example.myapplication.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.consts.Consts
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.SourceType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    operator fun invoke(sourceType: SourceType): Flow<PagingData<ImageEntity>> =
        imageRepository.getImagesPagingFlow(Consts.PAGE_SIZE, sourceType)

}
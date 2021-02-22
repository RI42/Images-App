package com.example.myapplication.domain.usecase

import androidx.paging.PagingData
import com.example.myapplication.consts.Consts
import com.example.myapplication.domain.ImageRepositoryProvider
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.SourceType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FetchImageUseCase @Inject constructor(
    private val imageRepositoryProvider: ImageRepositoryProvider
) {

    operator fun invoke(sourceType: SourceType): Flow<PagingData<ImageEntity>> =
        imageRepositoryProvider[sourceType].getImagesPagingFlow(Consts.PAGE_SIZE)

}
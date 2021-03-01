package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.ImageRepositoryProvider
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import javax.inject.Inject


class SetLikeUseCase @Inject constructor(
    private val imageRepositoryProvider: ImageRepositoryProvider
) {

    suspend operator fun invoke(item: ImageEntity) {
        imageRepositoryProvider[item.type].setState(item.id, ImageState.LIKE)
    }

}
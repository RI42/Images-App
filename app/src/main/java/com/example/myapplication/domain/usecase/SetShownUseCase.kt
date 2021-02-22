package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.ImageRepositoryProvider
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.SourceType
import javax.inject.Inject


class SetShownUseCase @Inject constructor(
    private val imageRepositoryProvider: ImageRepositoryProvider
) {

    suspend operator fun invoke(item: ImageEntity, type: SourceType) {
        imageRepositoryProvider[type].setShown(item.id)
    }

}
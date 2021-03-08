package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import javax.inject.Inject


class SetLikeUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(image: ImageEntity) {
        imageRepository.setState(image, ImageState.LIKE)
    }

}
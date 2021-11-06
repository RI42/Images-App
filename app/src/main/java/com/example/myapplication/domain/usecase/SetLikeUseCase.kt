package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.filter.ImageState
import javax.inject.Inject


class SetLikeUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(image: Image) {
        imageRepository.setState(image, ImageState.LIKE)
    }

}
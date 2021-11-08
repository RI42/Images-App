package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.filter.ImageState
import javax.inject.Inject


class ChangeLikeUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {

    suspend operator fun invoke(image: Image, liked: Boolean) {
        imageRepository.setState(image, if (liked) ImageState.LIKE else ImageState.DISLIKE)
    }

}
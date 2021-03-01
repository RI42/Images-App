package com.example.myapplication.model

import kotlinx.serialization.Serializable


@Serializable
data class ImageData(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
)

fun ImageData.toEntity(sourceType: SourceType) = ImageEntity(
    id = id,
    url = url,
    width = width,
    height = height,
    state = ImageState.NOT_SHOWN,
    type = sourceType
)
package com.example.myapplication.data.network.model

import com.example.myapplication.data.db.model.ImageEntity
import com.example.myapplication.domain.model.ImageState
import com.example.myapplication.domain.model.SourceType
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
)

fun ImageResponse.toEntity(sourceType: SourceType) = ImageEntity(
    id = id,
    url = url,
    width = width,
    height = height,
    state = ImageState.NOT_SHOWN,
    sourceType = sourceType
)
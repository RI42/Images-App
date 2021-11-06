package com.example.myapplication.data.db.model

import androidx.room.Entity
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.filter.ImageState
import com.example.myapplication.domain.model.filter.SourceType
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "image_data", primaryKeys = ["id", "sourceType"])
data class ImageEntity(
    val id: String,
    val sourceType: SourceType,
    val url: String,
    val width: Int,
    val height: Int,
    val state: ImageState
)

fun ImageEntity.toModel() = Image(
    id = id,
    sourceType = sourceType,
    url = url,
    width = width,
    height = height,
    state = state
)

fun Image.toEntity() = ImageEntity(
    id = id,
    sourceType = sourceType,
    url = url,
    width = width,
    height = height,
    state = state
)
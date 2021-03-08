package com.example.myapplication.model

import androidx.room.Entity
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


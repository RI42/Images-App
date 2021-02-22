package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "image_data")
data class ImageEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    val width: Int,
    val height: Int,
    val liked: Boolean = false,
    val isShown: Boolean = false,
    val type: SourceType
)
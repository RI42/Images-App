package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity
data class ImageData(
    @PrimaryKey
    val id: String,
    val url: String,
    val liked: Boolean = false
)
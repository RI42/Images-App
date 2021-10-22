package com.example.myapplication.domain.model


data class Image(
    val id: String,
    val sourceType: SourceType,
    val url: String,
    val width: Int,
    val height: Int,
    val state: ImageState
)


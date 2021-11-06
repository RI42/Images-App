package com.example.myapplication.domain.model

import com.example.myapplication.domain.model.filter.ImageState
import com.example.myapplication.domain.model.filter.SourceType


data class Image(
    val id: String,
    val sourceType: SourceType,
    val url: String,
    val width: Int,
    val height: Int,
    val state: ImageState
)


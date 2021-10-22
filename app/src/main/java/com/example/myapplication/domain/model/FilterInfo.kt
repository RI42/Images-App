package com.example.myapplication.domain.model

data class FilterInfo(
    val sourceType: Set<SourceType>,
    val imageState: Set<ImageState>
)

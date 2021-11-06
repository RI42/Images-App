package com.example.myapplication.domain.model.filter

data class FilterInfo(
    val sourceType: Set<SourceType>,
    val imageState: Set<ImageState>
)

//typealias FilterInfo = Set<Category>
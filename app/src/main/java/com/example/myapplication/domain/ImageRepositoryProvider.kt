package com.example.myapplication.domain

import com.example.myapplication.model.SourceType

interface ImageRepositoryProvider {
    operator fun get(sourceType: SourceType): ImageRepository
}
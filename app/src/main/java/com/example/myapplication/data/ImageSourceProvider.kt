package com.example.myapplication.data

import com.example.myapplication.domain.model.SourceType
import com.example.myapplication.data.network.TheApi

interface ImageSourceProvider {
    operator fun get(sourceType: SourceType): TheApi
}
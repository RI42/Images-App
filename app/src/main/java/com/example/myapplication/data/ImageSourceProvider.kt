package com.example.myapplication.data

import com.example.myapplication.data.network.TheApi
import com.example.myapplication.domain.model.filter.SourceType

interface ImageSourceProvider {
    operator fun get(sourceType: SourceType): TheApi
}
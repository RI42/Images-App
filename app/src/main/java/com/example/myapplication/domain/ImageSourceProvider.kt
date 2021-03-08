package com.example.myapplication.domain

import com.example.myapplication.model.SourceType
import com.example.myapplication.network.TheApi

interface ImageSourceProvider {
    operator fun get(sourceType: SourceType): TheApi
}
package com.example.myapplication.data

import com.example.myapplication.data.network.TheApi
import com.example.myapplication.domain.model.filter.SourceType
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ImageSourceProviderImpl @Inject constructor(
    private val apiProviders: MutableMap<SourceType, Provider<TheApi>>
) : ImageSourceProvider {

    override operator fun get(sourceType: SourceType): TheApi {
        return apiProviders.getValue(sourceType).get()
    }
}
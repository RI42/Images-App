package com.example.myapplication.data

import androidx.paging.ExperimentalPagingApi
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.ImageRepositoryProvider
import com.example.myapplication.model.SourceType
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class ImageRepositoryProviderImpl @Inject constructor(
    private val repositoryProviders: MutableMap<SourceType, Provider<ImageRepository>>
) : ImageRepositoryProvider {

    override operator fun get(sourceType: SourceType): ImageRepository {
        repositoryProviders.getValue(sourceType).get()
        return repositoryProviders.getValue(sourceType).get()
    }
}
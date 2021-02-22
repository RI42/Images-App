package com.example.myapplication.data

import androidx.paging.ExperimentalPagingApi
import com.example.myapplication.di.ImageRepositoryFactory
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.ImageRepositoryProvider
import com.example.myapplication.model.SourceType
import com.example.myapplication.network.TheApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class ImageRepositoryProviderImpl @Inject constructor(
    private val theApiMap: MutableMap<SourceType, TheApi>,
    private val imageRepositoryFactory: ImageRepositoryFactory
) : ImageRepositoryProvider {

    override operator fun get(sourceType: SourceType): ImageRepository {
        imageRepositoryFactory.get(theApiMap.getValue(sourceType), sourceType)
        return imageRepositoryFactory.get(theApiMap.getValue(sourceType), sourceType)
    }
}
package com.example.myapplication.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.ImageSourceProvider
import com.example.myapplication.model.FilterInfo
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import com.example.myapplication.model.SourceType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val imageSourceProvider: ImageSourceProvider,
) : ImageRepository {

    override fun getImagesPagingFlow(
        pageSize: Int,
        sourceType: SourceType
    ): Flow<PagingData<ImageEntity>> =
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            remoteMediator = PageKeyedRemoteMediator(
                db,
                imageSourceProvider[sourceType],
                sourceType
            )
        ) {
            db.imageDao().getNotShownImages(sourceType)
        }.flow

    override suspend fun setState(image: ImageEntity, state: ImageState) {
        db.imageDao().updateImage(image.copy(state = state))
    }

    override fun filteredImagesFlow(
        pageSize: Int,
        info: FilterInfo
    ): Flow<PagingData<ImageEntity>> = Pager(
        config = PagingConfig(pageSize = pageSize, enablePlaceholders = true),
    ) {
        db.imageDao().getFiltered(info.sourceType, info.imageState)
    }.flow
}
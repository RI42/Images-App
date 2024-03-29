package com.example.myapplication.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.myapplication.consts.Consts
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.db.model.toEntity
import com.example.myapplication.data.db.model.toModel
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.filter.FilterInfo
import com.example.myapplication.domain.model.filter.ImageState
import com.example.myapplication.domain.model.filter.SourceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val imageSourceProvider: ImageSourceProvider,
) : ImageRepository {

    override fun getImagesPagingFlow(sourceType: SourceType): Flow<PagingData<Image>> =
        Pager(
            config = PagingConfig(pageSize = Consts.PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PageKeyedRemoteMediator(
                db = db,
                theApi = imageSourceProvider[sourceType],
                sourceType = sourceType
            )
        ) {

            db.imageDao().getNotShownImages(sourceType)
                .also { Timber.tag("PageKeyedRemoteMediator").d("SOURCE") }
        }
            .flow.map { paging -> paging.map { it.toModel() } }

    override suspend fun setState(image: Image, state: ImageState) {
        db.imageDao().updateImage(image.copy(state = state).toEntity())
    }

    override fun filteredImagesFlow(info: FilterInfo): Flow<PagingData<Image>> = Pager(
        config = PagingConfig(pageSize = Consts.PAGE_SIZE, enablePlaceholders = true),
    ) { db.imageDao().getFiltered(info) }
        .flow.map { paging -> paging.map { it.toModel() } }
}
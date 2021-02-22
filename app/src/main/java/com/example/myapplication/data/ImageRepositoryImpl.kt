package com.example.myapplication.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.SourceType
import com.example.myapplication.network.TheApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@ExperimentalPagingApi
class ImageRepositoryImpl @AssistedInject constructor(
    private val db: AppDatabase,
    @Assisted private val theApi: TheApi,
    @Assisted private val sourceType: SourceType,
) : ImageRepository {

    init {
        Timber.d("sourceType: $sourceType")
    }

    override fun getImagesPagingFlow(pageSize: Int): Flow<PagingData<ImageEntity>> =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            remoteMediator = PageKeyedRemoteMediator(db, theApi, sourceType)
        ) {
            db.imageDao().getNotShownImages(sourceType)
        }.flow

    override suspend fun setShown(id: String) {
        db.imageDao().setShown(id)
    }
}
package com.example.myapplication.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.myapplication.data.db.AppDatabase
import com.example.myapplication.data.db.model.ImageEntity
import com.example.myapplication.data.network.TheApi
import com.example.myapplication.data.network.model.toEntity
import com.example.myapplication.domain.model.filter.ImageState
import com.example.myapplication.domain.model.filter.SourceType
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


@ExperimentalPagingApi
class PageKeyedRemoteMediator(
    private val db: AppDatabase,
    private val theApi: TheApi,
    private val sourceType: SourceType
) : RemoteMediator<Int, ImageEntity>() {

    private val theDao = db.imageDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>
    ): MediatorResult {
        return try {
            Timber.d("load: $loadType, state: $state")
            when (loadType) {
                LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
                else -> {
                    val count = db.imageDao().countElements(sourceType, ImageState.NOT_SHOWN)

                    if (count > state.config.initialLoadSize) return MediatorResult.Success(false)

                    val limit = when (loadType) {
                        LoadType.REFRESH -> state.config.initialLoadSize + state.config.prefetchDistance
                        else -> state.config.pageSize
                    }
                    val data = theApi.fetchImage(limit).map { it.toEntity(sourceType) }

                    db.withTransaction {
                        theDao.insertOrIgnore(data)
                    }

                    MediatorResult.Success(endOfPaginationReached = data.isEmpty())
                }
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}

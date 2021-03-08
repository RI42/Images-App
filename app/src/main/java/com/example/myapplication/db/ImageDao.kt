package com.example.myapplication.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import com.example.myapplication.model.SourceType
import com.example.myapplication.utils.BaseDao

@Dao
abstract class ImageDao : BaseDao<ImageEntity>() {

    fun getNotShownImages(sourceType: SourceType) = get(sourceType, ImageState.NOT_SHOWN)

    @Query("select count(*) from image_data where state = :state and sourceType = :sourceType")
    abstract suspend fun countElements(sourceType: SourceType, state: ImageState): Int

    @Query("select * from image_data where state = :state and sourceType = :sourceType")
    abstract fun get(sourceType: SourceType, state: ImageState): PagingSource<Int, ImageEntity>

    @Query("UPDATE image_data SET state = :state WHERE id = :id and sourceType = :sourceType")
    abstract suspend fun setState(id: String, sourceType: SourceType, state: ImageState)

    @Update
    abstract suspend fun updateImage(image: ImageEntity)

    @Query("select * from image_data where state in (:state) and sourceType in (:sourceType)")
    abstract fun getFiltered(
        sourceType: Set<SourceType>,
        state: Set<ImageState>
    ): PagingSource<Int, ImageEntity>
}
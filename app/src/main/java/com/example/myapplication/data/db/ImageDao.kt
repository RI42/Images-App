package com.example.myapplication.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.data.db.model.ImageEntity
import com.example.myapplication.domain.model.FilterInfo
import com.example.myapplication.domain.model.ImageState
import com.example.myapplication.domain.model.SourceType
import com.example.myapplication.data.db.utils.BaseDao

@Dao
abstract class ImageDao : BaseDao<ImageEntity>() {

    fun getNotShownImages(sourceType: SourceType) = get(sourceType, ImageState.NOT_SHOWN)

    @Query("select count(*) from image_data where state = :state and sourceType = :sourceType")
    abstract suspend fun countElements(sourceType: SourceType, state: ImageState): Int

    @Query("select * from image_data where state = :state and sourceType = :sourceType")
    abstract fun get(sourceType: SourceType, state: ImageState): PagingSource<Int, ImageEntity>

    @Query("UPDATE image_data SET state = :state WHERE id = :id AND sourceType = :sourceType")
    abstract suspend fun setState(id: String, sourceType: SourceType, state: ImageState)

    @Update
    abstract suspend fun updateImage(image: ImageEntity)

    @Query(
        """
        SELECT * FROM image_data 
        WHERE state NOT IN (:exclude) 
            AND (:sourceTypeSize = 0 OR sourceType IN (:sourceType)) 
            AND (:stateSize = 0 OR state IN (:state))
            """
    )
    abstract fun getFiltered(
        sourceType: Set<SourceType>,
        state: Set<ImageState>,
        sourceTypeSize: Int = sourceType.size,
        stateSize: Int = state.size,
        exclude: Set<ImageState> = setOf(ImageState.NOT_SHOWN)
    ): PagingSource<Int, ImageEntity>

    fun getFiltered(
        info: FilterInfo
    ): PagingSource<Int, ImageEntity> = getFiltered(
        info.sourceType,
        info.imageState,
    )
}
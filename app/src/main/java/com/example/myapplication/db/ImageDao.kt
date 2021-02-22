package com.example.myapplication.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.SourceType
import com.example.myapplication.util.BaseDao

@Dao
abstract class ImageDao : BaseDao<ImageEntity>() {

    @Query("select * from image_data where isShown = 0 and type = :sourceType")
    abstract fun getNotShownImages(sourceType: SourceType): PagingSource<Int, ImageEntity>

    @Query("DELETE FROM image_data WHERE type = :sourceType")
    abstract fun deleteSourceType(sourceType: SourceType)

    @Query("UPDATE image_data SET isShown = 1 WHERE id = :id")
    abstract suspend fun setShown(id: String)
}
package com.example.myapplication.db

import androidx.room.TypeConverter
import com.example.myapplication.model.ImageState

class ImageStateConverter {

    @TypeConverter
    fun from(imageState: ImageState) = imageState.id

    @TypeConverter
    fun to(id: Int) = ImageState.map.getValue(id)
}
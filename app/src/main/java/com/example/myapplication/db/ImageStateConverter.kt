package com.example.myapplication.db

import androidx.room.TypeConverter
import com.example.myapplication.model.ImageState

class ImageStateConverter {

    @TypeConverter
    fun fromImageState(imageState: ImageState) = imageState.id

    @TypeConverter
    fun toImageState(id: String) = ImageState.map.getValue(id)
}
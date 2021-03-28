package com.example.myapplication.db

import androidx.room.TypeConverter
import com.example.myapplication.model.SourceType

class SourceTypeConverter {

    @TypeConverter
    fun fromSourceType(sourceType: SourceType) = sourceType.id

    @TypeConverter
    fun toSourceType(id: String) = SourceType.map.getValue(id)
}
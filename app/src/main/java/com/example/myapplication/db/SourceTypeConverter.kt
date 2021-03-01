package com.example.myapplication.db

import androidx.room.TypeConverter
import com.example.myapplication.model.SourceType

class SourceTypeConverter {

    @TypeConverter
    fun from(sourceType: SourceType) = sourceType.id

    @TypeConverter
    fun to(id: Int) = SourceType.map.getValue(id)
}
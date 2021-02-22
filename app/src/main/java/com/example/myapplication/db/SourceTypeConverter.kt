package com.example.myapplication.db

import androidx.room.TypeConverter
import com.example.myapplication.model.SourceType

class SourceTypeConverter {
    @TypeConverter
    fun fromSourceType(sourceType: SourceType) = sourceType.value

    @TypeConverter
    fun toSourceType(value: Int) =
        SourceType.values().find { it.value == value } ?: error("No such SourceType value")
}
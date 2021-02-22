package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.model.ImageEntity

@Database(
    entities = [ImageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SourceTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao

    companion object {

        fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app.db"
        )
            .build()
    }
}

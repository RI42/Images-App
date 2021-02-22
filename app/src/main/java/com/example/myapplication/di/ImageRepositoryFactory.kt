package com.example.myapplication.di

import androidx.paging.ExperimentalPagingApi
import com.example.myapplication.data.ImageRepositoryImpl
import com.example.myapplication.model.SourceType
import com.example.myapplication.network.TheApi
import dagger.assisted.AssistedFactory

@AssistedFactory
@ExperimentalPagingApi
interface ImageRepositoryFactory {
    fun get(theApi: TheApi, sourceType: SourceType): ImageRepositoryImpl
}
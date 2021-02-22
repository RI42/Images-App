package com.example.myapplication.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.myapplication.BuildConfig
import com.example.myapplication.consts.Consts
import com.example.myapplication.data.ImageRepositoryProviderImpl
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.di.qualifiers.TheCatApi
import com.example.myapplication.di.qualifiers.TheDogApi
import com.example.myapplication.domain.ImageRepositoryProvider
import com.example.myapplication.model.SourceType
import com.example.myapplication.network.TheApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CoreModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.createDatabase(context)
    }

    @Singleton
    @Provides
    @TheCatApi
    @IntoMap
    @SourceTypeKey(SourceType.CAT)
    fun provideCatService(
        client: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): TheApi {
        Timber.d("provide CAT")
        return Retrofit.Builder()
            .baseUrl(Consts.THE_CAT_BASE_URL)
            .client(client)
            .addConverterFactory(jsonConverterFactory)
            .build()
            .create(TheApi::class.java)
    }

    @Singleton
    @Provides
    @TheDogApi
    @IntoMap
    @SourceTypeKey(SourceType.DOG)
    fun provideDogService(
        client: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): TheApi {
        Timber.d("provide DOG")
        return Retrofit.Builder()
            .baseUrl(Consts.THE_DOG_BASE_URL)
            .client(client)
            .addConverterFactory(jsonConverterFactory)
            .build()
            .create(TheApi::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        val timeout = 30L
        return OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())
}

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {

    @ExperimentalPagingApi
    @Binds
    fun bindsImage(imageRepositoryProviderImpl: ImageRepositoryProviderImpl): ImageRepositoryProvider
}
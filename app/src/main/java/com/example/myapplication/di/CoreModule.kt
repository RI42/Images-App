package com.example.myapplication.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.example.myapplication.BuildConfig
import com.example.myapplication.consts.Consts
import com.example.myapplication.data.ImageRepositoryImpl
import com.example.myapplication.data.ImageSourceProviderImpl
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.domain.ImageRepository
import com.example.myapplication.domain.ImageSourceProvider
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
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.createDatabase(context)
    }

    @Provides
    @Singleton
    @IntoMap
    @SourceTypeKey(SourceType.CAT)
    fun provideCatService(
        client: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): TheApi {
        return createApi(Consts.THE_CAT_BASE_URL, client, jsonConverterFactory)
    }

    @Provides
    @Singleton
    @IntoMap
    @SourceTypeKey(SourceType.DOG)
    fun provideDogService(
        client: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ): TheApi {
        return createApi(Consts.THE_DOG_BASE_URL, client, jsonConverterFactory)
    }

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideJsonConverterFactory(json: Json): Converter.Factory =
        json.asConverterFactory("application/json".toMediaType())
}

private fun createApi(
    baseUrl: String,
    client: OkHttpClient,
    jsonConverterFactory: Converter.Factory
): TheApi = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(client)
    .addConverterFactory(jsonConverterFactory)
    .build()
    .create(TheApi::class.java)

@Module
@InstallIn(SingletonComponent::class)
interface BindsModule {

    @Binds
    fun bindsImageSourceProvider(imageSourceProviderImpl: ImageSourceProviderImpl): ImageSourceProvider

    @ExperimentalPagingApi
    @Binds
    fun bindsImageRepository(imageRepositoryImpl: ImageRepositoryImpl): ImageRepository
}
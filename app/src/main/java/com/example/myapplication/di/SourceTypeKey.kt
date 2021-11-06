package com.example.myapplication.di

import com.example.myapplication.domain.model.filter.SourceType
import dagger.MapKey

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class SourceTypeKey(val value: SourceType)

package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterInfo(
    val sourceType: Set<SourceType>,
    val imageState: Set<ImageState>
) : Parcelable

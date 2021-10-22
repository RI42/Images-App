package com.example.myapplication.ui.pager

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.myapplication.domain.model.SourceType
import kotlinx.parcelize.Parcelize

@Parcelize
data class PageInfo(
    val type: SourceType,
    @StringRes
    val stringId: Int,
    @DrawableRes
    val iconId: Int
) : Parcelable



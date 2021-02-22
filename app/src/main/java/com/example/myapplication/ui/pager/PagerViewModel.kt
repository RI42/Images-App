package com.example.myapplication.ui.pager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.myapplication.domain.usecase.FetchImageUseCase
import com.example.myapplication.domain.usecase.SetShownUseCase
import com.example.myapplication.model.ImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PagerViewModel @ExperimentalPagingApi
@Inject constructor(
    private val fetchImageUseCase: FetchImageUseCase,
    private val setShownUseCase: SetShownUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val PAGE_INFO = "PAGE_INFO"
    }

    private val pagerInfo =
        savedStateHandle.get<PageInfo>(PAGE_INFO) ?: error("$PAGE_INFO is not found")

    val images = fetchImageUseCase(pagerInfo.type)
        .cachedIn(viewModelScope)

    fun setShown(item: ImageEntity) {
        viewModelScope.launch {
            setShownUseCase(item, pagerInfo.type)
        }
    }

    init {
        Timber.d("PagerViewModel $pagerInfo")
    }
}
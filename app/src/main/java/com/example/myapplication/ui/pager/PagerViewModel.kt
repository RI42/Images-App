package com.example.myapplication.ui.pager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.usecase.ChangeLikeUseCase
import com.example.myapplication.domain.usecase.FetchImagesUseCase
import com.example.myapplication.domain.usecase.SaveImageToStorageUseCase
import com.example.myapplication.ui.utils.checkCancellationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PagerViewModel @Inject constructor(
    private val fetchImagesUseCase: FetchImagesUseCase,
    private val changeLikeUseCase: ChangeLikeUseCase,
    private val saveImageToStorageUseCase: SaveImageToStorageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val PAGE_INFO = "PAGE_INFO"
    }

    private val pagerInfo =
        savedStateHandle.get<PageInfo>(PAGE_INFO) ?: error("$PAGE_INFO is not found")

    val images = fetchImagesUseCase(pagerInfo.type)
        .cachedIn(viewModelScope)

    val isLoading = MutableStateFlow(false)
    private val _msg = MutableSharedFlow<String>()
    val msg = _msg.asSharedFlow()

    init {
        Timber.d("$pagerInfo")
    }

    fun setLike(item: Image) {
        viewModelScope.launch {
            changeLikeUseCase(item, liked = true)
        }
    }

    fun setDislike(item: Image) {
        viewModelScope.launch {
            changeLikeUseCase(item, liked = false)
        }
    }

    fun saveImageToStorage(image: Image) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                saveImageToStorageUseCase(image)
                _msg.emit("Saved to Gallery")
            } catch (e: Exception) {
                e.checkCancellationException()
                Timber.d(e)
                _msg.emit("Failed to save image")
            } finally {
                isLoading.value = false
            }
        }
    }

}

package com.example.myapplication.ui.pager

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.myapplication.domain.usecase.FetchImageUseCase
import com.example.myapplication.domain.usecase.SaveImageToStorageUseCase
import com.example.myapplication.domain.usecase.SetDislikeUseCase
import com.example.myapplication.domain.usecase.SetLikeUseCase
import com.example.myapplication.model.ImageEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PagerViewModel @ExperimentalPagingApi
@Inject constructor(
    private val fetchImageUseCase: FetchImageUseCase,
    private val setLikeUseCase: SetLikeUseCase,
    private val setDislikeUseCase: SetDislikeUseCase,
    private val saveImageToStorageUseCase: SaveImageToStorageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val PAGE_INFO = "PAGE_INFO"
    }

    private val pagerInfo =
        savedStateHandle.get<PageInfo>(PAGE_INFO) ?: error("$PAGE_INFO is not found")

    val images = fetchImageUseCase(pagerInfo.type)
        .cachedIn(viewModelScope)

    val isLoading = MutableStateFlow(false)
    private val _msg = MutableSharedFlow<String>()
    val msg = _msg.asSharedFlow()

    init {
        Timber.d("PagerViewModel $pagerInfo")
    }

    fun setLike(item: ImageEntity) {
        viewModelScope.launch {
            setLikeUseCase(item)
        }
    }

    fun setDislike(item: ImageEntity) {
        viewModelScope.launch {
            setDislikeUseCase(item)
        }
    }

    fun saveMediaToStorage(image: ImageEntity) {
        isLoading.value = true
        viewModelScope.launch {
            val minTime = 1000
            val start = System.currentTimeMillis()
            try {
                saveImageToStorageUseCase(image)
                _msg.emit("Saved to Gallery")
            } catch (e: Exception) {
                _msg.emit("Failed to save image")
                Timber.d(e)
            } finally {
                val end = System.currentTimeMillis()
                if (end - start < minTime) {
                    delay(minTime - (end - start))
                }
                isLoading.value = false
            }
        }
    }

}

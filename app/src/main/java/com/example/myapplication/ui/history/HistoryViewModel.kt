package com.example.myapplication.ui.history

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.myapplication.domain.usecase.FilteredImagesUseCase
import com.example.myapplication.domain.usecase.SaveImageToStorageUseCase
import com.example.myapplication.model.FilterInfo
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import com.example.myapplication.model.SourceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    val filteredImagesUseCase: FilteredImagesUseCase,
    val saveImageToStorageUseCase: SaveImageToStorageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val filterInfo = MutableStateFlow(
        savedStateHandle["filterInfo"] ?: FilterInfo(
            mutableSetOf(SourceType.CAT),
            ImageState.values().drop(1).toMutableSet()
        )
    )

    val catChecker = MutableLiveData(SourceType.CAT in filterInfo.value.sourceType)
    val dogChecker = MutableLiveData(SourceType.DOG in filterInfo.value.sourceType)
    val likedChecker = MutableLiveData(ImageState.LIKE in filterInfo.value.imageState)
    val dislikedChecker = MutableLiveData(ImageState.DISLIKE in filterInfo.value.imageState)

    init {
        fun changeSourceType(checked: Boolean, value: SourceType) {
            val collection = filterInfo.value.sourceType
            val newCollection = if (checked) collection + value else collection - value
            filterInfo.value = filterInfo.value.copy(sourceType = newCollection)
        }

        fun changeImageState(checked: Boolean, value: ImageState) {
            val collection = filterInfo.value.imageState
            val newCollection = if (checked) collection + value else collection - value
            filterInfo.value = filterInfo.value.copy(imageState = newCollection)
        }

        catChecker.asFlow()
            .onEach { changeSourceType(it, SourceType.CAT) }
            .launchIn(viewModelScope)
        dogChecker.asFlow()
            .onEach { changeSourceType(it, SourceType.DOG) }
            .launchIn(viewModelScope)
        likedChecker.asFlow()
            .onEach { changeImageState(it, ImageState.LIKE) }
            .launchIn(viewModelScope)
        dislikedChecker.asFlow()
            .onEach { changeImageState(it, ImageState.DISLIKE) }
            .launchIn(viewModelScope)
    }

    val images = filterInfo
        .onEach { savedStateHandle["filterInfo"] = it }
        .flatMapLatest { filteredImagesUseCase(it) }
        .cachedIn(viewModelScope)

    val isLoading = MutableStateFlow(false)
    private val _msg = MutableSharedFlow<String>()
    val msg = _msg.asSharedFlow()

    fun saveImageToStorage(image: ImageEntity) {
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

/*

    val posts = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty<RedditPost>() },
        savedStateHandle.getLiveData<String>(KEY_SUBREDDIT)
            .asFlow()
            .flatMapLatest { repository.postsOfSubreddit(it, 30) }
            // cachedIn() shares the paging state across multiple consumers of posts,
            // e.g. different generations of UI across rotation config change
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

 */
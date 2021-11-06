package com.example.myapplication.ui.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.filter.FilterInfo
import com.example.myapplication.domain.model.filter.ImageState
import com.example.myapplication.domain.model.filter.SourceType
import com.example.myapplication.domain.usecase.FilteredImagesUseCase
import com.example.myapplication.domain.usecase.SaveImageToStorageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val filteredImagesUseCase: FilteredImagesUseCase,
    private val saveImageToStorageUseCase: SaveImageToStorageUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val filterInfo = MutableStateFlow(
        savedStateHandle["filterInfo"] ?: FilterInfo(setOf(), setOf())
    )

    val catChecker = MutableStateFlow(SourceType.CAT in filterInfo.value.sourceType)
    val dogChecker = MutableStateFlow(SourceType.DOG in filterInfo.value.sourceType)
    val likedChecker = MutableStateFlow(ImageState.LIKE in filterInfo.value.imageState)
    val dislikedChecker = MutableStateFlow(ImageState.DISLIKE in filterInfo.value.imageState)

    val images = filterInfo
        .onEach { savedStateHandle["filterInfo"] = it }
        .flatMapLatest { filteredImagesUseCase(it) }
        .cachedIn(viewModelScope)

    val isLoading = MutableStateFlow(false)
    private val _msg = MutableSharedFlow<String>()
    val msg = _msg.asSharedFlow()

    init {

        merge(
            catChecker.map { SourceType.CAT to it },
            dogChecker.map { SourceType.DOG to it }
        )
            .onEach { changeFilter(it) }
            .launchIn(viewModelScope)

        merge(
            likedChecker.map { ImageState.LIKE to it },
            dislikedChecker.map { ImageState.DISLIKE to it }
        )
            .onEach { changeFilter(it) }
            .launchIn(viewModelScope)
    }

    @JvmName("changeFilterSourceType")
    private fun changeFilter(entry: Pair<SourceType, Boolean>) {
        val (value, checked) = entry
        val collection = filterInfo.value.sourceType
        val newCollection = if (checked) collection + value else collection - value
        filterInfo.value = filterInfo.value.copy(sourceType = newCollection)
    }

    @JvmName("changeFilterImageState")
    private fun changeFilter(entry: Pair<ImageState, Boolean>) {
        val (value, checked) = entry
        val collection = filterInfo.value.imageState
        val newCollection = if (checked) collection + value else collection - value
        filterInfo.value = filterInfo.value.copy(imageState = newCollection)
    }

    fun saveImageToStorage(image: Image) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                saveImageToStorageUseCase(image)
                _msg.emit("Saved to Gallery")
            } catch (e: Exception) {
                _msg.emit("Failed to save image")
                Timber.d(e)
            } finally {
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
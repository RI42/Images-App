package com.example.myapplication.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.myapplication.domain.usecase.FilteredImagesUseCase
import com.example.myapplication.domain.usecase.SaveImageToStorageUseCase
import com.example.myapplication.model.FilterInfo
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.ImageState
import com.example.myapplication.model.SourceType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    companion object {
        private const val MIN_PROGRESS_TIME_MS = 1000
    }

    private val filterInfo = MutableStateFlow(
        savedStateHandle["filterInfo"] ?: FilterInfo(setOf(), setOf())
    )

    val catChecker = MutableLiveData(SourceType.CAT in filterInfo.value.sourceType)
    val dogChecker = MutableLiveData(SourceType.DOG in filterInfo.value.sourceType)
    val likedChecker = MutableLiveData(ImageState.LIKE in filterInfo.value.imageState)
    val dislikedChecker = MutableLiveData(ImageState.DISLIKE in filterInfo.value.imageState)

    val images = filterInfo
        .onEach { savedStateHandle["filterInfo"] = it }
        .flatMapLatest { filteredImagesUseCase(it) }
        .cachedIn(viewModelScope)

    val isLoading = MutableStateFlow(false)
    private val _msg = MutableSharedFlow<String>()
    val msg = _msg.asSharedFlow()

    init {
        merge(
            catChecker.asFlow().map { it to SourceType.CAT },
            dogChecker.asFlow().map { it to SourceType.DOG }
        )
            .onEach { changeSourceType(it.first, it.second) }
            .launchIn(viewModelScope)

        merge(
            likedChecker.asFlow().map { it to ImageState.LIKE },
            dislikedChecker.asFlow().map { it to ImageState.DISLIKE }
        )
            .onEach { changeImageState(it.first, it.second) }
            .launchIn(viewModelScope)
    }

    private fun changeSourceType(checked: Boolean, value: SourceType) {
        val collection = filterInfo.value.sourceType
        val newCollection = if (checked) collection + value else collection - value
        filterInfo.value = filterInfo.value.copy(sourceType = newCollection)
    }

    private fun changeImageState(checked: Boolean, value: ImageState) {
        val collection = filterInfo.value.imageState
        val newCollection = if (checked) collection + value else collection - value
        filterInfo.value = filterInfo.value.copy(imageState = newCollection)
    }

    fun saveImageToStorage(image: ImageEntity) {
        isLoading.value = true
        viewModelScope.launch {
            val start = System.currentTimeMillis()
            try {
                saveImageToStorageUseCase(image)
                _msg.emit("Saved to Gallery")
            } catch (e: Exception) {
                _msg.emit("Failed to save image")
                Timber.d(e)
            } finally {
                val end = System.currentTimeMillis()
                if (end - start < MIN_PROGRESS_TIME_MS) {
                    delay(MIN_PROGRESS_TIME_MS - (end - start))
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
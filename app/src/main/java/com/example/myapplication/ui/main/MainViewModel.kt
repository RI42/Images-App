package com.example.myapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ImageData
import com.example.myapplication.network.Order
import com.example.myapplication.network.TheCatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val theCatApi: TheCatApi
) : ViewModel() {

    private val _image = MutableStateFlow(ImageData("", ""))
    val image = _image.asStateFlow()

    fun fetchImage() {
        viewModelScope.launch {
            try {
                _image.value = theCatApi.fetchImage(1, Order.RAND)[0]
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
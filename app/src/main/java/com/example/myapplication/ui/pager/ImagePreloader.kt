package com.example.myapplication.ui.pager

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.domain.model.Image

class ImagePreloader(
    val context: Context,
    val adapter: RecyclerView.Adapter<*>,
    val preloadSize: Int,
    val getItem: (Int) -> Image?
) {

    private var currentSize = 0

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            preload()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (positionStart >= preloadSize) return
            currentSize = (currentSize - itemCount).coerceAtLeast(0)
            preload()
        }
    }

    fun register() {
        adapter.registerAdapterDataObserver(observer)
    }

    fun unregister() {
        adapter.unregisterAdapterDataObserver(observer)
    }

    private fun preload() {
        while (currentSize < adapter.itemCount && currentSize < preloadSize) {
            preloadImage(getItem(currentSize) ?: return)
            ++currentSize
        }
    }

    private fun preloadImage(image: Image) {
        Glide.with(context)
            .load(image.url)
            .preload(image.width, image.height)
    }
}
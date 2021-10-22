package com.example.myapplication.ui.pager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.RvItemBinding
import com.example.myapplication.domain.model.Image
import timber.log.Timber

class ImageAdapter(
    val context: Context
) : PagingDataAdapter<Image, ImageAdapter.ImageViewHolder>(DiffCallback) {

    private var currentSize = 0
    private val maxPreloadSize = 10

    init {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                preload()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (positionStart >= maxPreloadSize) return
                currentSize = (currentSize - itemCount).coerceAtLeast(0)
                preload()
            }
        })
    }

    private fun preload() {
        while (currentSize < itemCount && currentSize < maxPreloadSize) {
            preloadImage(getItem(currentSize) ?: return)
            ++currentSize
        }
    }

    private fun preloadImage(image: Image) {
        Glide.with(context)
            .load(image.url)
            .preload(image.width, image.height)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.bind(currentItem)
        Timber.d("onBindViewHolder position: $position, currentItem $currentItem")
    }


    fun getItemByPos(position: Int) =
        if (position != RecyclerView.NO_POSITION) getItem(position) else null

    inner class ImageViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {

            Glide.with(context)
                .load(image.url)
                .override(image.width, image.height)
                .into(binding.img)
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
        return oldItem == newItem
    }
}
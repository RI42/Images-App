package com.example.myapplication.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.HistoryRvItemBinding
import com.example.myapplication.domain.model.Image
import timber.log.Timber

class HistoryImageAdapter(
    val context: Context
) : PagingDataAdapter<Image, HistoryImageAdapter.ImageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            HistoryRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.bind(currentItem)
        Timber.d("onBindViewHolder position: $position, currentItem $currentItem")
    }


    fun getItemByPos(position: Int) =
        if (position != RecyclerView.NO_POSITION) getItem(position) else null

    inner class ImageViewHolder(private val binding: HistoryRvItemBinding) :
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
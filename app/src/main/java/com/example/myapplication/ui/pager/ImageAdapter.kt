package com.example.myapplication.ui.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.databinding.RvItemBinding
import com.example.myapplication.model.ImageEntity

class ImageAdapter : PagingDataAdapter<ImageEntity, ImageAdapter.ImageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position)
    }

    class ImageViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: ImageEntity?, position: Int) {
            if (image!=null) {
                binding.tv.text = position.toString()
                binding.img.load(image.url)
            } else {
                binding.tv.text = "loading$position"
            }
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<ImageEntity>() {
    override fun areItemsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImageEntity, newItem: ImageEntity): Boolean {
        return oldItem == newItem
    }

}
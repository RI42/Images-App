package com.example.myapplication.ui.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.example.myapplication.R
import com.example.myapplication.databinding.RvItemBinding
import com.example.myapplication.domain.model.Image
import timber.log.Timber

class ImageAdapter(
    private val requestManager: RequestManager,
    diffCallback: DiffUtil.ItemCallback<Image> = DiffCallback
) : PagingDataAdapter<Image, ImageAdapter.ImageViewHolder>(diffCallback),
    ListPreloader.PreloadModelProvider<Image> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.bind(currentItem)
    }

    fun getItemByPos(position: Int) =
        if (position != RecyclerView.NO_POSITION) getItem(position) else null

    inner class ImageViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            requestManager
                .load(image.url)
                .placeholder(R.drawable.ic_cat)
                .override(image.width, image.height)
                .into(binding.img)
        }
    }

    override fun getPreloadItems(position: Int): List<Image> {
        Timber.d("getPreloadItems: $position")
        return (if (position != RecyclerView.NO_POSITION) {
            listOfNotNull(getItem(position))
        } else listOf()).also { Timber.d("getPreloadItems: $it") }
    }

    override fun getPreloadRequestBuilder(item: Image): RequestBuilder<*> {
        return requestManager.load(item.url).override(item.width, item.height)
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
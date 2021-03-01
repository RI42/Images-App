package com.example.myapplication.ui.pager

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.myapplication.databinding.RvItemBinding
import com.example.myapplication.model.ImageEntity
import timber.log.Timber

typealias ItemClickListener<T> = (View, T) -> Unit

class ImageAdapter(
    val context: Context,
    val clickListener: ItemClickListener<ImageEntity>? = null
) :
    PagingDataAdapter<ImageEntity, ImageAdapter.ImageViewHolder>(DiffCallback) {

    private var cnt = 0
    private val preloadSize = 10

    init {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                preload()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (positionStart >= preloadSize) return
                cnt = (cnt - itemCount).coerceAtLeast(0)
                preload()
            }
        })
    }

    private fun preload() {
        while (cnt < itemCount && cnt < preloadSize) {
            preload(getItem(cnt) ?: return)
            ++cnt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return

        val animFade = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0f, 1f).apply {
            duration = 300
            interpolator = AccelerateInterpolator()
            start()
        }

        holder.bind(currentItem)
        Timber.d("onBindViewHolder position: $position, currentItem $currentItem")
    }

    private fun preload(next: ImageEntity) {
        val request = ImageRequest.Builder(context)
            .diskCachePolicy(CachePolicy.DISABLED)
            .size(width = next.width, height = next.height)
            .data(next.url)
            .build()

        Coil.enqueue(request)
    }

    fun getItemByPos(position: Int) =
        if (position != RecyclerView.NO_POSITION) getItem(position) else null

    inner class ImageViewHolder(private val binding: RvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var image: ImageEntity

        init {
            val viewClickListener = clickListener?.let { listener ->
                View.OnClickListener { listener(it, image) }
            }

            binding.download.setOnClickListener(viewClickListener)
        }

        fun bind(image: ImageEntity) {
            this.image = image
            binding.img.load(image.url) {
                diskCachePolicy(CachePolicy.DISABLED)
                crossfade(true)
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
package com.example.myapplication.ui.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader.PreloadSizeProvider
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.example.myapplication.R
import com.example.myapplication.databinding.PagerFragmentBinding
import com.example.myapplication.domain.model.Image
import com.example.myapplication.ui.pager.PagerViewModel.Companion.PAGE_INFO
import com.example.myapplication.ui.utils.ImageSavingHelper
import com.example.myapplication.ui.utils.viewBinding
import com.example.myapplication.ui.utils.viewLifecycleScope
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class PagerFragment : Fragment(R.layout.pager_fragment) {

    companion object {

        @JvmStatic
        fun newInstance(pageInfo: PageInfo) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PAGE_INFO, pageInfo)
                }
            }
    }

    private val imageSaver = ImageSavingHelper(this) { model.saveImageToStorage(it) }

    private val pageInfo by lazy { requireArguments().getParcelable<PageInfo>(PAGE_INFO)!! }
    private val model: PagerViewModel by viewModels()
    private val binding by viewBinding(PagerFragmentBinding::bind)


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestManager = Glide.with(this)
        val preloadSize = PreloadSizeProvider<Image> { item, adapterPosition, perItemPosition ->
            intArrayOf(item.width, item.height)
        }
        val adapter = ImageAdapter(requestManager)
        val preloader = RecyclerViewPreloader(requestManager, adapter, preloadSize, 10)


        val layoutManager = object : LinearLayoutManager(binding.rv.context) {
            override fun canScrollVertically() = false
        }
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Timber.d("viewHolder.bindingAdapterPosition ${viewHolder.bindingAdapterPosition}")
                val item = adapter.getItemByPos(viewHolder.bindingAdapterPosition) ?: return

                when (direction) {
                    ItemTouchHelper.LEFT -> model.setDislike(item)
                    ItemTouchHelper.RIGHT -> model.setLike(item)
                }
                // TODO PAGING Перевести пагинацию на scroll
//                binding.rv.smoothScrollToPosition(viewHolder.bindingAdapterPosition + 1)
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.25f
            }
        }).apply {
            attachToRecyclerView(binding.rv)
        }
        val snapHelper = PagerSnapHelper().apply {
            attachToRecyclerView(binding.rv)
        }

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = layoutManager
        binding.rv.addOnScrollListener(preloader)
        binding.rv.adapter = adapter

        viewLifecycleScope.launchWhenCreated {
            model.images
                .collectLatest {
                    adapter.submitData(it)
                }
        }

        fun onClick(view: View) {
            if (binding.rv.isAnimating) return
            val item = adapter.getItemByPos(layoutManager.findFirstVisibleItemPosition()) ?: return
            when (view.id) {
                R.id.dislike -> model.setDislike(item)
                R.id.like -> model.setLike(item)
            }
            // TODO PAGING Перевести пагинацию на scroll
//            binding.rv.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1)
        }

        binding.dislike.setOnClickListener(::onClick)
        binding.like.setOnClickListener(::onClick)
        binding.download.setOnClickListener {
            val image = adapter.getItemByPos(layoutManager.findFirstVisibleItemPosition())
                ?: return@setOnClickListener
            saveImage(it, image)
        }

        val fadeThrough = MaterialFadeThrough()
        viewLifecycleScope.launchWhenCreated {
            model.isLoading
                .onEach {
                    // Begin watching for changes in the View hierarchy.
                    TransitionManager.beginDelayedTransition(binding.container, fadeThrough)
                    binding.download.isInvisible = it
                    binding.progress.isVisible = it
                    delay(800)
                }
                .launchIn(this)

            model.msg
                .onEach { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                .launchIn(this)
        }
    }

    private fun saveImage(view: View, image: Image) {
        imageSaver.saveImage(image)
    }

}

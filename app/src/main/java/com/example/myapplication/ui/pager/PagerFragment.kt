package com.example.myapplication.ui.pager

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.map
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.databinding.PagerFragmentBinding
import com.example.myapplication.ui.pager.PagerViewModel.Companion.PAGE_INFO
import com.example.myapplication.util.dataBinding
import com.example.myapplication.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
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

    private val model: PagerViewModel by viewModels()
    private val binding: PagerFragmentBinding by dataBinding() // { viewModel = model }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImageAdapter()
        val layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically() = false
        }

        val touchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
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
                    val item = adapter.getItemByPos(viewHolder.bindingAdapterPosition) ?: error("item not found")
                    model.setShown(item)
                }
            }
        )
        touchHelper.attachToRecyclerView(binding.rv)

//        var anim: ViewPropertyAnimator? = null
        binding.btn.setOnClickListener {
            Timber.d("layoutManager.findFirstVisibleItemPosition ${layoutManager.findFirstVisibleItemPosition()}")
            val item = adapter.getItemByPos(layoutManager.findFirstVisibleItemPosition())
                ?: error("item not found")
            model.setShown(item)
        }


        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = adapter
        viewLifecycleScope.launchWhenCreated {
            model.images
                .map { pagingData ->
                    pagingData.map {
                        val request = ImageRequest.Builder(requireContext())
                            .data(it.url)
                            .build()
                        Coil.enqueue(request)
                        it
                    }
                }
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }
}

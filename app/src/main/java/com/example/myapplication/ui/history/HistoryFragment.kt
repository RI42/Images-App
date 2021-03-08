package com.example.myapplication.ui.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FilterDialogBinding
import com.example.myapplication.databinding.HistoryFragmentBinding
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.utils.ImageSavingHelper
import com.example.myapplication.utils.MainNavigationFragment
import com.example.myapplication.utils.viewBinding
import com.example.myapplication.utils.viewLifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HistoryFragment : MainNavigationFragment(R.layout.history_fragment) {

    private val model by viewModels<HistoryViewModel>()
    private val binding by viewBinding(HistoryFragmentBinding::bind)

    private val imageSaver = ImageSavingHelper(this) { model.saveImageToStorage(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appbar.applyInsetter {
            type(statusBars = true) { padding(top = true) }
        }

        binding.rv.applyInsetter {
            type(navigationBars = true) { padding(bottom = true) }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> {
                    val filterBinding = FilterDialogBinding.inflate(layoutInflater).apply {
                        viewModel = model
                    }

                    MaterialAlertDialogBuilder(requireContext())
                        .setView(filterBinding.root)
                        .setPositiveButton(android.R.string.ok) { dialog, which -> }
                        .show()

                    true
                }
                else -> true
            }
        }

        val adapter = HistoryImageAdapter(requireContext())
        binding.rv.setHasFixedSize(true)
        binding.rv.adapter = adapter

        viewLifecycleScope.launchWhenCreated {
            model.images
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun saveImage(view: View, image: ImageEntity) {
        imageSaver.saveImage(image)
    }
}

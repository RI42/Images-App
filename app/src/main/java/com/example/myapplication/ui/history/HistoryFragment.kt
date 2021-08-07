package com.example.myapplication.ui.history

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.example.myapplication.R
import com.example.myapplication.databinding.FilterDialogBinding
import com.example.myapplication.databinding.HistoryFragmentBinding
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.utils.ImageSavingHelper
import com.example.myapplication.utils.MainNavigationFragment
import com.example.myapplication.utils.dataBinding
import com.example.myapplication.utils.viewLifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.delay

@AndroidEntryPoint
class HistoryFragment : MainNavigationFragment(R.layout.history_fragment) {

    private val model by viewModels<HistoryViewModel>()
    private val binding by dataBinding<HistoryFragmentBinding>()

    private val imageSaver = ImageSavingHelper(this) { model.saveImageToStorage(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.appbar.applyInsetter {
//            type(statusBars = true) { padding(top = true) }
//        }

//        binding.toolbar.applyInsetter {
//            type(statusBars = true) { padding(top = true) }
//        }

        binding.rv.applyInsetter {
            type(navigationBars = true) { padding(bottom = true) }
        }

//        binding.but.applyInsetter {
//            type(navigationBars = true) { padding(bottom = true) }
//        }

//        binding.rv.doOnLayout {
//            binding.rv.updatePadding(bottom = binding.but.height)
//        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)

            val transform = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(MaterialContainerTransform().apply {
                    startView = binding.big
                    endView = binding.small
                    this.addTarget(endView as View)
//                    fadeMode = MaterialContainerTransform. FADE_MODE_OUT
                    scrimColor = Color.TRANSPARENT
                })
                .apply {
//                    ordering = ORDERING_TOGETHER
                }

            TransitionManager.beginDelayedTransition(binding.appbar, transform)
            binding.big.isVisible = false
            binding.small.isVisible = true

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


//                    val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
//                        override fun onStateChanged(bottomSheet: View, newState: Int) {
//                            when (newState) {
//                                BottomSheetBehavior.STATE_EXPANDED -> Timber.d("STATE_EXPANDED")
//                                BottomSheetBehavior.STATE_COLLAPSED -> Timber.d("STATE_COLLAPSED")
//                                BottomSheetBehavior.STATE_DRAGGING -> Timber.d("STATE_DRAGGING")
//                                BottomSheetBehavior.STATE_SETTLING -> Timber.d("STATE_SETTLING")
//                                BottomSheetBehavior.STATE_HIDDEN -> Timber.d("STATE_HIDDEN")
//                                BottomSheetBehavior.STATE_HALF_EXPANDED -> Timber.d("STATE_HALF_EXPANDED")
//                                else -> return
//                            }
//                        }
//
//                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//                    }
//
//                    val dialog = BottomSheetDialog(
//                        requireContext(),
//                        R.style.ThemeOverlay_Demo_BottomSheetDialog
//                    ).apply {
//                        setContentView(R.layout.fragment_bottom_sheet_demo_dialog)
//                        behavior.addBottomSheetCallback(bottomSheetCallback)
//                        window?.let { window ->
//                            window.findViewById<View>(R.id.container).fitsSystemWindows = false
//                            WindowCompat.setDecorFitsSystemWindows(window, false)
//                        }
//
//                    }
//                    dialog.show()
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

package com.example.myapplication.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentBottomSheetDemoDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDemoDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDemoDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetDemoDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> Timber.d("STATE_EXPANDED")
//                    BottomSheetBehavior.STATE_COLLAPSED -> Timber.d("STATE_COLLAPSED")
//                    BottomSheetBehavior.STATE_DRAGGING -> Timber.d("STATE_DRAGGING")
//                    BottomSheetBehavior.STATE_SETTLING -> Timber.d("STATE_SETTLING")
//                    BottomSheetBehavior.STATE_HIDDEN -> Timber.d("STATE_HIDDEN")
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> Timber.d("STATE_HALF_EXPANDED")
//                    else -> return
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
//        }
//
//        BottomSheetBehavior.from(binding.dialogRoot).apply {
//            addBottomSheetCallback(bottomSheetCallback)
//        }
    }


    companion object {

        fun newInstance(): BottomSheetDemoDialogFragment =
            BottomSheetDemoDialogFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
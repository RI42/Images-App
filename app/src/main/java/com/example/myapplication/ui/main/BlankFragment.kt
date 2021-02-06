package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.BlankFragmentBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.util.dataBinding
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding

class BlankFragment : MainNavigationFragment(R.layout.blank_fragment) {


    private val viewModel: BlankViewModel by viewModels()
    private val binding: BlankFragmentBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.applySystemWindowInsetsToPadding(top = true)
    }

}
package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.util.dataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : MainNavigationFragment(R.layout.main_fragment) {

    private val model: MainViewModel by viewModels()
    private val binding: MainFragmentBinding by dataBinding() // { viewModel = model }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btn.setOnClickListener {
            findNavController().navigate(R.id.goto_blank)
        }
//        Insetter.builder()
//            .applySystemWindowInsetsToMargin(Side.TOP)
//            .applySystemWindowInsetsToPadding(Side.BOTTOM)
//            .applyToView(binding.nestedScrollView)

    }


}
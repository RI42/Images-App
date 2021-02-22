package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.ui.pager.PagerAdapter
import com.example.myapplication.util.dataBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : MainNavigationFragment(R.layout.main_fragment) {

    private val model: MainViewModel by viewModels()
    private val binding: MainFragmentBinding by dataBinding() // { viewModel = model }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = PagerAdapter(this)
        binding.pager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            val pageInfo = PagerAdapter.pageType[position]
            tab.icon = AppCompatResources.getDrawable(requireContext(), pageInfo.iconId)
            tab.text = getString(pageInfo.stringId)
        }.attach()
    }


}
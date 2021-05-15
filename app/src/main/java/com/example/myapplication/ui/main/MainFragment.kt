package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.ui.pager.PagerAdapter
import com.example.myapplication.utils.MainNavigationFragment
import com.example.myapplication.utils.dataBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlin.math.abs

@AndroidEntryPoint
class MainFragment : MainNavigationFragment(R.layout.main_fragment) {

    private val model: MainViewModel by viewModels({ requireParentFragment() })
    private val binding: MainFragmentBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appbar.applyInsetter {
            type(statusBars = true) { padding(top = true) }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.history -> {
                    findNavController().navigate(R.id.goto_history)
                    true
                }
                else -> true
            }
        }

        binding.pager.adapter = PagerAdapter(this)
        binding.pager.isUserInputEnabled = false
        binding.pager.setPageTransformer { page, position ->
            when {
                position <= -1 -> {  // (-Infinity,-1]
                    // This page is way off-screen to the left.
                    page.translationX = position
                }
                position < 1 -> { // (-1,1)
                    page.alpha = 1 - abs(position)
                    page.translationX = -position * page.width
//                    page.scaleX = max(MIN_SCALE, 1 - abs(position))
//                    page.scaleY = max(MIN_SCALE, 1 - abs(position))
                }
                else -> {  // [1,+Infinity)
                    // This page is way off-screen to the right.
//                    page.alpha = 1f
                    page.translationX = position
                }
            }
        }

        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            val pageInfo = PagerAdapter.pageType[position]
            tab.icon = AppCompatResources.getDrawable(requireContext(), pageInfo.iconId)
            tab.text = getString(pageInfo.stringId)
        }.attach()
    }
}
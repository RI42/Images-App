package com.example.myapplication.ui.pager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.R
import com.example.myapplication.model.SourceType

class PagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    companion object {
        val pageType = arrayOf(
            PageInfo(SourceType.CAT, R.string.cat, R.drawable.ic_cat),
            PageInfo(SourceType.DOG, R.string.dog, R.drawable.ic_dog)
        )
    }

    override fun getItemCount() = pageType.size

    override fun createFragment(position: Int) = PagerFragment.newInstance(pageType[position])
}
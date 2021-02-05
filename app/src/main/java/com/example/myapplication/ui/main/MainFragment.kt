package com.example.myapplication.ui.main

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.myapplication.R
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.util.dataBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainFragment : MainNavigationFragment(R.layout.main_fragment) {

    private val model: MainViewModel by viewModels()
    private val binding: MainFragmentBinding by dataBinding() // { viewModel = model }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}
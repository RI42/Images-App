package com.example.myapplication.ui.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

val Fragment.viewLifecycleScope get() = viewLifecycleOwner.lifecycleScope
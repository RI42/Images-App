package com.example.myapplication.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

val Fragment.viewLifecycleScope get() = viewLifecycleOwner.lifecycleScope
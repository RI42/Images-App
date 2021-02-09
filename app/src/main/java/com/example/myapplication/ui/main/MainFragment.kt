package com.example.myapplication.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import coil.load
import com.example.myapplication.R
import com.example.myapplication.databinding.MainFragmentBinding
import com.example.myapplication.ui.MainNavigationFragment
import com.example.myapplication.util.dataBinding
import com.example.myapplication.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : MainNavigationFragment(R.layout.main_fragment) {

    private val model: MainViewModel by viewModels()
    private val binding: MainFragmentBinding by dataBinding() // { viewModel = model }

    val l = registerForActivityResult(ActivityResultContracts.GetContent()) {
        binding.img.setImageURI(it)
    }


    @SuppressLint("BinaryOperationInTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.save.setOnClickListener {
            l.launch("image/*")
        }

        binding.fetch.setOnClickListener {
            model.fetchImage()
        }

        val internalFilesDir = requireContext().filesDir
        val internalCacheDir = requireContext().cacheDir
        val externalFilesDir = requireContext().getExternalFilesDir(null)
        val externalCacheDir = requireContext().externalCacheDir
        val externalFilesDirs = requireContext().externalCacheDirs
//        val externalStorageDir = Environment.getExternalStorageDirectory()

        Timber.d(
            "a\ninternalFilesDir: $internalFilesDir" +
                    "\ninternalCacheDir: $internalCacheDir" +
                    "\nexternalFilesDir: $externalFilesDir" +
                    "\nexternalCacheDir: $externalCacheDir" +
                    "\nexternalFilesDirs: ${externalFilesDirs.toList()}" +
                    "\ngetExternalStorageState: ${Environment.getExternalStorageState()}" +
                    "\nisExternalStorageEmulated: ${Environment.isExternalStorageEmulated()}" +
                    "\nisExternalStorageLegacy: ${Environment.isExternalStorageLegacy()}" +
                    "\nisExternalStorageRemovable: ${Environment.isExternalStorageRemovable()}"
        )

//        val dir = externalFilesDir!!
//        val f = File(dir, "myFile")
//        f.writeText("123")
//        Timber.d("===== create ${f.exists()}")
////        Timber.d("===== create ${f.createNewFile()}")
//        Timber.d("===== ${dir.listFiles()?.toList()}")


        viewLifecycleScope.launchWhenStarted {
            model.image
                .onEach {
                    if (it.url.isNotBlank()) binding.img.load(it.url)
                }
                .launchIn(this)
        }

    }


}
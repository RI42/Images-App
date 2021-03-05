package com.example.myapplication.ui.pager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.myapplication.R
import com.example.myapplication.databinding.PagerFragmentBinding
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.ui.pager.PagerViewModel.Companion.PAGE_INFO
import com.example.myapplication.ui.rvUtils.StackLayoutManager
import com.example.myapplication.utils.viewBinding
import com.example.myapplication.utils.viewLifecycleScope
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import timber.log.Timber



@AndroidEntryPoint
class PagerFragment : Fragment(R.layout.pager_fragment) {

    companion object {

        @JvmStatic
        fun newInstance(pageInfo: PageInfo) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PAGE_INFO, pageInfo)
                }
            }
    }

    private val pageInfo by lazy { requireArguments().getParcelable<PageInfo>(PAGE_INFO)!! }
    private val model: PagerViewModel by viewModels()
    private val binding by viewBinding(PagerFragmentBinding::bind)

    private val register = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        Timber.d("RequestPermission $it")
        if (it) {
            Toast.makeText(requireContext(), "granted", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fadeThrough = MaterialFadeThrough()

        // Begin watching for changes in the View hierarchy.
        TransitionManager.beginDelayedTransition(binding.root, fadeThrough)


        val adapter = ImageAdapter(requireContext())
//        val layoutManager = object : LinearLayoutManager(requireContext()) {
//            override fun canScrollVertically() = false
//        }

//        val layoutManager = object : StackLayoutManager(ScrollOrientation.BOTTOM_TO_TOP, 1) {
//            override fun canScrollVertically() = false
//        }

        val layoutManager = object : StackLayoutManager(horizontalLayout = false, maxViews = 1) {
            override fun canScrollVertically() = false
        }

        val touchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    Timber.d("viewHolder.bindingAdapterPosition ${viewHolder.bindingAdapterPosition}")
                    val item = adapter.getItemByPos(viewHolder.bindingAdapterPosition)
                        ?: error("item not found")

                    when (direction) {
                        ItemTouchHelper.LEFT -> model.setDislike(item)
                        ItemTouchHelper.RIGHT -> model.setLike(item)
                    }
                }
            }
        ).apply {
            attachToRecyclerView(binding.rv)
        }

        val snapHelper = PagerSnapHelper().apply {
            attachToRecyclerView(binding.rv)
        }

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = adapter
        viewLifecycleScope.launchWhenCreated {
            model.images
//                .map { pagingData ->
//                    pagingData.map {
//                        it to false
//                    }
//                }
                .collectLatest {
                    adapter.submitData(it)
                }
        }

        fun onClick(view: View) {
            if (binding.rv.isAnimating) return
            val item = adapter.getItemByPos(layoutManager.findFirstVisibleItemPosition())
                ?: return
            when (view.id) {
                R.id.dislike -> model.setDislike(item)
                R.id.like -> model.setLike(item)
            }
        }

        binding.dislike.setOnClickListener(::onClick)
        binding.like.setOnClickListener(::onClick)
        binding.download.setOnClickListener {
            val image = adapter.getItemByPos(layoutManager.findFirstVisibleItemPosition())
                ?: return@setOnClickListener
            saveImage(it, image)
        }

        viewLifecycleScope.launchWhenCreated {
            model.isLoading
                .onEach {
                    binding.download.isInvisible = it
                    binding.progress.isVisible = it
                }
                .launchIn(this)

            model.msg
                .onEach { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                .launchIn(this)
        }

    }

    private fun saveImage(view: View, image: ImageEntity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageToStorage(image)
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    saveImageToStorage(image)
                }
                shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) -> {
                    Toast.makeText(requireContext(), "cmon", Toast.LENGTH_SHORT).show()
                    register.launch(WRITE_EXTERNAL_STORAGE)
                }
                else -> {
                    register.launch(WRITE_EXTERNAL_STORAGE)
                }
            }
        }

    }

    private fun saveImageToStorage(image: ImageEntity) {
        model.saveMediaToStorage(image)
    }


//    private fun saveMediaToStorage(bitmap: Bitmap) {
//        val filename = "${System.currentTimeMillis()}.jpg"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            saveQ(filename, bitmap)
//        } else {
//            saveLegacy(filename, bitmap)
//        }
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.Q)
//    private fun saveQ(filename: String, bitmap: Bitmap) {
//        requireContext().contentResolver?.let { resolver ->
//            val contentValues = ContentValues().apply {
//                put(MediaColumns.DISPLAY_NAME, filename)
//                put(MediaColumns.MIME_TYPE, "image/jpg")
//                put(MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//            }
//            val imageUri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                ?: throw RuntimeException("Failed to get image Uri")
//            resolver.openOutputStream(imageUri)?.use {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
//                Toast.makeText(requireContext(), "Saved to Photos", Toast.LENGTH_SHORT).show()
//            } ?: throw RuntimeException("Failed to save image")
//        } ?: throw RuntimeException("Failed to save image")
//    }
//
//    private fun saveLegacy(filename: String, bitmap: Bitmap) {
//        val imagesDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//        val image = File(imagesDir, filename)
//        FileOutputStream(image).use {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
//            Toast.makeText(requireContext(), "Saved to Photos", Toast.LENGTH_SHORT).show()
//        }
//    }


}

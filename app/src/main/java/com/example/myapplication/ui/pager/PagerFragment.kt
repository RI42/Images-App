package com.example.myapplication.ui.pager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.drawable.MovieDrawable
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.databinding.PagerFragmentBinding
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.ui.pager.PagerViewModel.Companion.PAGE_INFO
import com.example.myapplication.utils.viewBinding
import com.example.myapplication.utils.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

        val adapter = ImageAdapter(requireContext(), ::saveImage)
        val layoutManager = object : LinearLayoutManager(requireContext()) {
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
    }

    private fun saveImage(view: View, image: ImageEntity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            load(image)
        } else {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(requireContext(), "PERMISSION_GRANTED", Toast.LENGTH_SHORT)
                        .show()
                    load(image)
                }
                shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) -> {
                    Toast.makeText(requireContext(), "cmon", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    register.launch(WRITE_EXTERNAL_STORAGE)
                }
            }
        }

    }

    private fun load(image: ImageEntity) = lifecycleScope.launch {
        val request = ImageRequest.Builder(requireContext())
            .data(image.url)
            .size(width = image.width, height = image.height)
            .build()
        try {
            val drawable = Coil.execute(request).drawable!!
            saveMediaToStorage(image.url, drawable)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    private fun saveMediaToStorage(url: String, drawable: Drawable) {

        requireContext().contentResolver?.let { resolver ->
            val filename = url.substring(url.lastIndexOf('/') + 1)
            val extension = filename.substring(filename.lastIndexOf('.') + 1)
            val mime = when (extension) {
                "gif" -> "image/gif"
                "png" -> "image/png"
                else -> "image/jpeg"
            }

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            Timber.d("filename $filename, mime $mime")
            val path = Environment.DIRECTORY_PICTURES + "/${pageInfo.type.name}"
            val contentValues = ContentValues().apply {
                put(MediaColumns.DISPLAY_NAME, filename)
                put(MediaColumns.MIME_TYPE, mime)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaColumns.RELATIVE_PATH, path)
                    put(MediaColumns.IS_PENDING, 1)
                }
            }

            val imageUri = resolver.insert(collection, contentValues)
                ?: throw RuntimeException("Failed to get image Uri")
            if (extension == "gif") {
                //todo gif
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    drawable as AnimatedImageDrawable
                } else {
                    drawable as MovieDrawable
                }

                val bitmap = drawable.toBitmap()
                resolver.openOutputStream(imageUri)?.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    Toast.makeText(requireContext(), "Saved to $path", Toast.LENGTH_SHORT).show()
                } ?: throw RuntimeException("Failed to save image")
            } else {
                val bitmap = (drawable as BitmapDrawable).bitmap

                val compressFormat = when (extension) {
                    "png" -> Bitmap.CompressFormat.PNG
                    else -> Bitmap.CompressFormat.JPEG
                }
                resolver.openOutputStream(imageUri)?.use {
                    bitmap.compress(compressFormat, 100, it)
                    Toast.makeText(requireContext(), "Saved to $path", Toast.LENGTH_SHORT).show()
                } ?: throw RuntimeException("Failed to save image")
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaColumns.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
            }

        } ?: throw RuntimeException("Failed to save image")
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

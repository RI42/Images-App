package com.example.myapplication.ui.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.domain.model.Image
import timber.log.Timber

class ImageSavingHelper(
    private val fragment: Fragment,
    private val onFailure: ((Image) -> Unit)? = null,
    private val onSuccess: ((Image) -> Unit)? = null
) {

    private val context get() = fragment.requireContext()
    private lateinit var image: Image

    private val register =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Timber.d("RequestPermission $isGranted")
            if (isGranted) {
                Toast.makeText(context, "granted", Toast.LENGTH_SHORT).show()
                onSuccess?.invoke(image)
            } else {
                onFailure?.invoke(image)
            }
        }

    fun saveImage(image: Image) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onSuccess?.invoke(image)
        } else {
            this.image = image
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    onSuccess?.invoke(image)
                }
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    Toast.makeText(context, "cmon", Toast.LENGTH_SHORT).show()
                    register.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                else -> {
                    register.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }
}
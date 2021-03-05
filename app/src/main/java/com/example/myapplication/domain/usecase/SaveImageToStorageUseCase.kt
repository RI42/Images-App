package com.example.myapplication.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.example.myapplication.model.ImageEntity
import com.example.myapplication.model.SourceType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject

class SaveImageToStorageUseCase @Inject constructor(
    @ApplicationContext val context: Context
) {

    suspend operator fun invoke(image: ImageEntity) = withContext(Dispatchers.IO) {
        val file = getFile(image)
        saveMediaToStorage(image, file)
    }

    @WorkerThread
    private fun getFile(image: ImageEntity) = Glide.with(context)
        .asFile()
        .load(image.url)
        .submit(image.width, image.height)
        .get()

    private fun getDirName(type: SourceType) =
        "${type.name[0]}${type.name.substring(1).toLowerCase(Locale.getDefault())}s"

    private fun getExtension(filename: String) = filename.substring(filename.lastIndexOf('.') + 1)
    private fun getFileNameFromUrl(url: String) = url.substring(url.lastIndexOf('/') + 1)
    private fun getMimeTypeByExtension(extension: String) = when (extension) {
        "gif" -> "image/gif"
        "png" -> "image/png"
        else -> "image/jpeg"
    }

    private fun saveMediaToStorage(image: ImageEntity, file: File) {
        context.contentResolver?.let { resolver ->
            val filename = getFileNameFromUrl(image.url)
            val extension = getExtension(filename)
            val mime = getMimeTypeByExtension(extension)

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            Timber.d("filename $filename, mime $mime")

            val path = "${Environment.DIRECTORY_PICTURES}/${getDirName(image.type)}"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mime)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, path)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            resolver.insert(collection, contentValues)?.let { imageUri ->
                resolver.openFileDescriptor(imageUri, "w")?.use {
                    ParcelFileDescriptor.AutoCloseOutputStream(it).write(file.readBytes())
                } ?: throw RuntimeException("Failed to save image")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
            }
        } ?: throw RuntimeException("Failed to save image")
    }
}
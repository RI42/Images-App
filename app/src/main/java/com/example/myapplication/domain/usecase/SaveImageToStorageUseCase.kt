package com.example.myapplication.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.annotation.RequiresApi
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveQ(image, file)
        } else {
            saveLegacy(image, file)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveQ(image: ImageEntity, file: File) {
        context.contentResolver?.let { resolver ->
            val filename = getFileNameFromUrl(image.url)
            val extension = getExtension(filename)
            val mime = getMimeTypeByExtension(extension)
            val path = Environment.DIRECTORY_PICTURES + File.separator + getDirName(image.type)
            val collection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            Timber.d("filename $filename, mime $mime, path $path")

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, mime)
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            resolver.insert(collection, contentValues)?.let { imageUri ->
                resolver.openFileDescriptor(imageUri, "w")?.use {
                    ParcelFileDescriptor.AutoCloseOutputStream(it).write(file.readBytes())
                } ?: throw RuntimeException("FileDescriptor is null")

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)

            } ?: throw RuntimeException("imageUri is null")
        } ?: throw RuntimeException("contentResolver is null")
    }

    private fun saveLegacy(image: ImageEntity, file: File) {
        val filename = getFileNameFromUrl(image.url)
        val extension = getExtension(filename)
        val mime = getMimeTypeByExtension(extension)
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath +
                    File.separator +
                    getDirName(image.type)
        Timber.d("filename $filename, path $path")
        val img = File(path, filename)
        file.copyTo(img)

        MediaScannerConnection.scanFile(
            context,
            arrayOf(img.path),
            arrayOf(mime),
            null
        )
    }
}
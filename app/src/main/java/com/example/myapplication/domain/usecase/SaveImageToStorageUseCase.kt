package com.example.myapplication.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.example.myapplication.domain.model.Image
import com.example.myapplication.domain.model.SourceType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

// TODO: зарефакторить
class SaveImageToStorageUseCase @Inject constructor(
    @ApplicationContext val context: Context
) {

    suspend operator fun invoke(image: Image) = withContext(Dispatchers.IO) {
        val file = getFile(image)
        saveMediaToStorage(image, file)
    }

    @WorkerThread
    private fun getFile(image: Image) = Glide.with(context)
        .asFile()
        .load(image.url)
        .submit(image.width, image.height)
        .get()

    private fun saveMediaToStorage(image: Image, file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveQ(image, file)
        } else {
            saveLegacy(image, file)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveQ(image: Image, file: File) {
        context.contentResolver?.let { resolver ->
            val filename = FileName(image.url)
            val path =
                Environment.DIRECTORY_PICTURES + File.separator + getDirName(image.sourceType)
            val collection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            Timber.d("filename $filename, path $path")

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename.fullName)
                put(MediaStore.Images.Media.MIME_TYPE, filename.mimeType)
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

    private fun saveLegacy(image: Image, file: File) {
        val filename = FileName(image.url)
        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath +
                    File.separator +
                    getDirName(image.sourceType)
        Timber.d("filename $filename, path $path")

        var img = File(path, filename.fullName)
        var count = 1
        while (img.exists()) {
            img = File(path, "${filename.title} (${count++}).${filename.extension}")
        }
        file.copyTo(img)

        MediaScannerConnection.scanFile(
            context,
            arrayOf(img.path),
            arrayOf(filename.mimeType),
            null
        )
    }
}

private fun getDirName(type: SourceType) =
    "${type.name[0]}${type.name.substring(1).lowercase()}s"

private fun getNameFromUrl(url: String) = url.substring(url.lastIndexOf('/') + 1)

private data class FileName(
    val title: String,
    val extension: String,
    val mimeType: String
) {
    val fullName get() = "$title.$extension"
}

private fun FileName(url: String): FileName {
    val name = getNameFromUrl(url)
    val (title, extension) = name.split('.')
    val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).orEmpty()
    return FileName(title, extension, mime)
}

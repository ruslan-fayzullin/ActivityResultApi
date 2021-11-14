package com.example.activityresultapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakePhotoContract : ActivityResultContract<File, Uri>() {

    lateinit var photoUri: Uri

    override fun createIntent(context: Context, input: File?): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri: Uri = input?.let {
            FileProvider.getUriForFile(
                context,
                "com.example.android.fileprovider",
                createFile(it)
            )
        } ?: Uri.EMPTY
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        photoUri = uri
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? =
        intent?.getParcelableExtra(MediaStore.EXTRA_OUTPUT) ?: photoUri

    private fun createFile(storageDir: File): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}
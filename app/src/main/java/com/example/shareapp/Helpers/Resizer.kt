package com.example.shareapp.Helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

class Resizer {
    companion object {
        fun resizeImage(file: File, scaleTo: Int = 1024) {
            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, bmOptions)
            val photoW = bmOptions.outWidth
            val photoH = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / scaleTo, photoH / scaleTo)

            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor

            val resized = BitmapFactory.decodeFile(file.absolutePath, bmOptions) ?: return
            file.outputStream().use {
                resized.compress(Bitmap.CompressFormat.JPEG, 75, it)
                resized.recycle()
            }
        }
    }
}
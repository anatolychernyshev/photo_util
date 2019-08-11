package com.messapps.photorotator

import android.content.Context
import android.graphics.Bitmap
import io.reactivex.Flowable
import java.io.File
import java.io.File.separator
import java.io.IOException

class FileCompressor(context: Context) {

    // max width and height values of the compressed image is taken as 612x816
    private var maxWidth = 612
    private var maxHeight = 816
    private var compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    private var quality = 80
    private var destinationDirectoryPath = context.cacheDir.path + separator + "images"

    fun setMaxWidth(maxWidth: Int): FileCompressor {
        this.maxWidth = maxWidth
        return this
    }

    fun setMaxHeight(maxHeight: Int): FileCompressor {
        this.maxHeight = maxHeight
        return this
    }

    fun setCompressFormat(compressFormat: Bitmap.CompressFormat): FileCompressor {
        this.compressFormat = compressFormat
        return this
    }

    fun setQuality(quality: Int): FileCompressor {
        this.quality = quality
        return this
    }

    fun setDestinationDirectoryPath(destinationDirectoryPath: String): FileCompressor {
        this.destinationDirectoryPath = destinationDirectoryPath
        return this
    }

    @Throws(IOException::class)
    fun compressToFile(imageFile: File): File {
        return compressToFile(imageFile, imageFile.name)
    }

    @Throws(IOException::class)
    fun compressToFile(imageFile: File, compressedFileName: String): File {
        return ImageUtil.compressImage(
            imageFile, maxWidth, maxHeight, compressFormat, quality,
            destinationDirectoryPath + separator + compressedFileName
        )
    }

    @Throws(IOException::class)
    fun compressToBitmap(imageFile: File): Bitmap {
        return ImageUtil.decodeSampledBitmapFromFile(imageFile, maxWidth, maxHeight)
    }

    fun compressToFileAsFlowable(imageFile: File): Flowable<File> {
        return compressToFileAsFlowable(imageFile, imageFile.name)
    }

    fun compressToFileAsFlowable(imageFile: File, compressedFileName: String): Flowable<File> {
        return Flowable.defer {
            try {
                Flowable.just(compressToFile(imageFile, compressedFileName))
            } catch (e: IOException) {
                Flowable.error<File>(e)
            }
        }
    }

    fun compressToBitmapAsFlowable(imageFile: File): Flowable<Bitmap> {
        return Flowable.defer {
            try {
                Flowable.just(compressToBitmap(imageFile))
            } catch (e: IOException) {
                Flowable.error<Bitmap>(e)
            }
        }
    }
}
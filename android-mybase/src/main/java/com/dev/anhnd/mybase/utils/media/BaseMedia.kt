package com.dev.anhnd.mybase.utils.media

import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

sealed class BaseMedia(open val typeMedia: MediaType,
                       open var id: Long = -1L,
                       open var path: String = "",
                       open var dateAdded: Long = -1
) : MediaModelBase() {

    fun getFile(): File {
        return File(path)
    }

    fun getFileName(): String {
        return File(path).name
    }

    fun getFileExtension(): String {
        return File(path).extension
    }

    fun getFileNameWithoutExtension(): String {
        return File(path).nameWithoutExtension
    }

    fun getFileLastModify() : Long {
        return File(path).lastModified()
    }

    fun getFileLastModify(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(Date(getFileLastModify()))
    }

    fun getFileDateAdded(pattern: String = "yyyy-MM-dd"): String {
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(Date(dateAdded * 1000))
    }
}

open class BaseVideo(@MediaInfo(MediaStore.Video.VideoColumns._ID)
                     override var id: Long = -1L,
                     @MediaInfo(MediaStore.Video.VideoColumns.DATA)
                     override var path: String = "",
                     @MediaInfo(MediaStore.Video.VideoColumns.DATE_ADDED)
                     override var dateAdded: Long = -1,
                     @MediaInfo(MediaStore.Video.VideoColumns.SIZE)
                     var size: Float = -1f,
                     override var typeMedia: MediaType = MediaType.VIDEO
) : BaseMedia(
    typeMedia = typeMedia
) {
    override fun getUri(): Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
}

open class BaseAudio(@MediaInfo(MediaStore.Audio.AudioColumns._ID)
                     override var id: Long = -1L,
                     @MediaInfo(MediaStore.Audio.AudioColumns.DATA)
                     override var path: String = "",
                     @MediaInfo(MediaStore.Audio.AudioColumns.DATE_ADDED)
                     override var dateAdded: Long = -1,
                     @MediaInfo(MediaStore.Video.VideoColumns.SIZE)
                     var size: Float = -1f,
                     override var typeMedia: MediaType = MediaType.AUDIO
) : BaseMedia(
    typeMedia = typeMedia
) {
    override fun getUri(): Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
}

open class BaseImage(@MediaInfo(MediaStore.Images.ImageColumns._ID)
                     override var id: Long = -1L,
                     @MediaInfo(MediaStore.Images.ImageColumns.DATA)
                     override var path: String = "",
                     @MediaInfo(MediaStore.Images.ImageColumns.DATE_ADDED)
                     override var dateAdded: Long = -1,
                     override var typeMedia: MediaType = MediaType.IMAGE
) : BaseMedia(
    typeMedia = typeMedia
) {
    override fun getUri(): Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
}

enum class MediaType {
    VIDEO, AUDIO, IMAGE
}

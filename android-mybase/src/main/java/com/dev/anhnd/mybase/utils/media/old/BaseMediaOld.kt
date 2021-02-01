package com.dev.anhnd.mybase.utils.media.old

import android.net.Uri
import android.os.Parcelable
import android.util.Size
import kotlinx.android.parcel.Parcelize

@Deprecated(message = "Not support this class, migrate to BaseMedia")
sealed class BaseMedia(
        open val name: String = "",
        open val nameWithoutExtension: String = "",
        open val extension: String = "",
        open val uri: Uri? = null,
        open val path: String = "",
        open val pathFolderParent: String = "",
        open val bucketId: Int = -1,
        open val bucketName: String = "",
        open val lastTimeModify: Long = 0L,
        open val size: Float = 0f,
        open val typeMedia: MediaType,
)

@Parcelize
data class BaseVideo(
        override val name: String = "",
        override val nameWithoutExtension: String = "",
        override val extension: String = "",
        override val uri: Uri? = null,
        override val path: String = "",
        override val pathFolderParent: String = "",
        override val bucketId: Int = -1,
        override val bucketName: String = "",
        override val size: Float = 0f,
        override val lastTimeModify: Long = 0L,
        override val typeMedia: MediaType = MediaType.VIDEO,
        val duration: Long = 0L,
        val resolution: Size = Size(0, 0)
) : BaseMedia(
        name = name,
        nameWithoutExtension = nameWithoutExtension,
        extension = extension,
        uri = uri,
        path = path,
        pathFolderParent = pathFolderParent,
        bucketId = bucketId,
        bucketName = bucketName,
        size = size,
        lastTimeModify = lastTimeModify,
        typeMedia = typeMedia
), Parcelable

@Parcelize
data class BaseAudio(
        override val name: String = "",
        override val nameWithoutExtension: String = "",
        override val extension: String = "",
        override val uri: Uri? = null,
        override val path: String = "",
        override val pathFolderParent: String = "",
        override val bucketId: Int = -1,
        override val bucketName: String = "",
        override val size: Float = 0f,
        override val lastTimeModify: Long = 0L,
        override val typeMedia: MediaType = MediaType.AUDIO,
        val duration: Long = 0L,
) : BaseMedia(
        name = name,
        nameWithoutExtension = nameWithoutExtension,
        extension = extension,
        uri = uri,
        path = path,
        pathFolderParent = pathFolderParent,
        bucketId = bucketId,
        bucketName = bucketName,
        size = size,
        lastTimeModify = lastTimeModify,
        typeMedia = typeMedia
), Parcelable

@Parcelize
data class BaseImage(
        override val name: String = "",
        override val nameWithoutExtension: String = "",
        override val extension: String = "",
        override val uri: Uri? = null,
        override val path: String = "",
        override val pathFolderParent: String = "",
        override val bucketId: Int = -1,
        override val bucketName: String = "",
        override val size: Float = 0f,
        override val lastTimeModify: Long = 0L,
        override val typeMedia: MediaType = MediaType.IMAGE,
        val resolution: Size = Size(0, 0)
) : BaseMedia(
        name = name,
        nameWithoutExtension = nameWithoutExtension,
        extension = extension,
        uri = uri,
        path = path,
        pathFolderParent = pathFolderParent,
        bucketId = bucketId,
        bucketName = bucketName,
        size = size,
        lastTimeModify = lastTimeModify,
        typeMedia = typeMedia
), Parcelable

enum class MediaType {
    VIDEO, AUDIO, IMAGE
}

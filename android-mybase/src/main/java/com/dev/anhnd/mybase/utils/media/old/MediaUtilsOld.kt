package com.dev.anhnd.mybase.utils.media.old

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import com.dev.anhnd.mybase.utils.app.getApplication
import com.dev.anhnd.mybase.utils.log.LogDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

@Deprecated(message = "Not support this class, migrate to MediaUtils")
object MediaUtilsOld {

    private val TAG = MediaUtilsOld::class.java.simpleName

    suspend fun <T : BaseMedia> getAllMedia(
        selection: String? = null,
        selectionArgs: Array<out String>? = null,
        sortOrder: String? = null,
        t: T
    ): MutableList<T>? = withContext(Dispatchers.IO) {
        val medias = mutableListOf<T>()
        try {
            val contentUri = getContentUri(t)
            val projection = getProjection(t)
            val query = getApplication().contentResolver.query(
                contentUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
            query?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = getCursorId(t, cursor)
                    val uri: Uri = getUri(t, id)
                    val bucketId = getBucketId(t, cursor)
                    val bucketName = getBucketName(t, cursor)
                    val size = getSize(t, cursor)
                    val path = getPath(t, cursor)
                    val file = File(path)
                    if (file.exists()) {
                        medias += (addFileToModel(file, uri, bucketId, bucketName, size, t))
                    }
                }
                cursor.close()
            }
            query?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            LogDebug.d(TAG, "${e.message}")
            return@withContext null
        }
        return@withContext medias
    }

    private fun <E : BaseMedia> getCursorId(e: E?, cursor: Cursor): Long {
        return when (e) {
            is BaseVideo -> {
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
            }
            is BaseAudio -> {
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            }
            else -> {
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            }
        }
    }

    private fun <E : BaseMedia> getUri(e: E?, id: Long): Uri {
        return when (e) {
            is BaseVideo -> {
                ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            }
            is BaseAudio -> {
                ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            }
            else -> {
                ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            }
        }
    }

    private fun <E : BaseMedia> getBucketId(e: E?, cursor: Cursor): Int {
        return when (e) {
            is BaseVideo -> {
                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID))
            }
            is BaseAudio -> {
                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_ID))
            }
            else -> {
                cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID))
            }
        }
    }

    private fun <E : BaseMedia> getBucketName(e: E?, cursor: Cursor): String {
        return when (e) {
            is BaseVideo -> {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
            }
            is BaseAudio -> {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
            }
            else -> {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
            }
        }
    }

    private fun <E : BaseMedia> getSize(e: E?, cursor: Cursor): Float {
        return when (e) {
            is BaseVideo -> {
                cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
            }
            is BaseAudio -> {
                cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
            }
            else -> {
                cursor.getFloat(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
            }
        }
    }

    private fun <E : BaseMedia> getPath(e: E?, cursor: Cursor): String {
        return when (e) {
            is BaseVideo -> {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
            }
            is BaseAudio -> {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            }
            else -> {
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <E : BaseMedia> addFileToModel(
        file: File,
        uri: Uri,
        bucketId: Int,
        bucketName: String,
        size: Float,
        e: E?
    ): E {
        return when (e) {
            is BaseVideo -> {
                BaseVideo(
                    name = file.name,
                    nameWithoutExtension = file.nameWithoutExtension,
                    extension = file.extension,
                    uri = uri,
                    path = file.absolutePath,
                    pathFolderParent = file.parent,
                    bucketId = bucketId,
                    bucketName = bucketName,
                    size = size,
                    lastTimeModify = file.lastModified(),
                    duration = file.getMediaDuration()
                ) as E
            }
            is BaseAudio -> {
                BaseAudio(
                    name = file.name,
                    nameWithoutExtension = file.nameWithoutExtension,
                    extension = file.extension,
                    uri = uri,
                    path = file.absolutePath,
                    pathFolderParent = file.parent,
                    bucketId = bucketId,
                    bucketName = bucketName,
                    size = size,
                    lastTimeModify = file.lastModified(),
                    duration = file.getMediaDuration()
                ) as E
            }
            else -> {
                BaseImage(
                    name = file.name,
                    nameWithoutExtension = file.nameWithoutExtension,
                    extension = file.extension,
                    uri = uri,
                    path = file.absolutePath,
                    pathFolderParent = file.parent,
                    bucketId = bucketId,
                    bucketName = bucketName,
                    size = size,
                    lastTimeModify = file.lastModified()
                ) as E
            }
        }
    }

    private fun <E : BaseMedia> getContentUri(e: E?): Uri {
        return when (e) {
            is BaseVideo -> {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
            is BaseAudio -> {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            else -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        }
    }

    private fun <E : BaseMedia> getProjection(e: E?): Array<String>? {
        return when (e) {
            is BaseVideo -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    arrayOf(
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.BUCKET_ID,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media.DATA
                    )
                } else {
                    arrayOf(
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.BUCKET_ID,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media.DATA
                    )
                }
            }
            is BaseAudio -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.BUCKET_ID,
                        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA
                    )
                } else {
                    arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.BUCKET_ID,
                        MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Audio.Media.DATA
                    )
                }
            }
            is BaseImage -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                    )
                } else {
                    arrayOf(
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media.DATA
                    )
                }
            }
            else -> {
                return null
            }
        }
    }

    private fun File.getMediaDuration(): Long {
        if (!exists()) return 0
        var duration = -1L
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(getApplication(), Uri.fromFile(this))
            duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()!!
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
            return duration
        }
        return duration
    }

    private fun File.getMediaResolution(): Size {
        if (!exists()) return Size(0, 0)
        var size = Size(0, 0)
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(getApplication(), Uri.fromFile(this))
            val w: Int
            val h: Int
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                w = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_WIDTH)
                    ?.toInt()!!
                h = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_HEIGHT)
                    ?.toInt()!!
            } else {
                w = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_WIDTH)
                    ?.toInt()!!
                h = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_HEIGHT)
                    ?.toInt()!!
            }

            size = Size(w, h)
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
            LogDebug.e(TAG, "${e.message}")
            return size
        }
        return size
    }

    private fun insertFileToMedia(output: String, name:String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.TITLE, name)
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*")
        values.put(MediaStore.MediaColumns.SIZE, File(output).length())
        values.put(MediaStore.Audio.Media.ARTIST, "none")
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true)
        values.put(MediaStore.Audio.Media.IS_ALARM, true)
        values.put(MediaStore.Audio.Media.IS_MUSIC, false)
        val newUri: Uri?
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            values.put(MediaStore.Audio.Media.DATA, output)
            newUri = getApplication().contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            newUri = getApplication().contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            try {
                getApplication().contentResolver.openOutputStream(newUri!!).use { os ->
                    val file = File(output)
                    val size = file.length()
                    val bytes = ByteArray(size.toInt())
                    val buf = BufferedInputStream(FileInputStream(file))
                    buf.read(bytes, 0, bytes.size)
                    buf.close()

                    os?.write(bytes)
                    os?.close()
                    os?.flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Exception: ${e.message}")
            }
        }
        return newUri
    }
}
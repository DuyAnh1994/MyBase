package com.dev.anhnd.mybase.utils.file

import android.media.MediaMetadataRetriever
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.dev.anhnd.mybase.utils.app.getApplication
import com.dev.anhnd.mybase.utils.app.runOnMainThread
import com.dev.anhnd.mybase.utils.log.LogDebug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat

object FileUtils {

    private val TAG = FileUtils::class.java.simpleName

    private val ROOT_FOLDER = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        getApplication().getExternalFilesDir(null)!!.absolutePath
    } else {
        Environment.getExternalStorageDirectory().absolutePath
    }

    private const val FOLDER_APP = ""

    fun getChildMyFolder(child: String): String {
        val path = "$ROOT_FOLDER/${FOLDER_APP}/$child"
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdir()
        }
        return path
    }

    fun getPathIncrease(name: String, extension: String, childFolder: String): String {
        val pathWithoutExtension = "${getChildMyFolder(childFolder)}/$name"
        var index = 1
        val decimalFormat = DecimalFormat("00")
        var path = "${pathWithoutExtension}_${decimalFormat.format(index)}.$extension"
        while (File(path).exists()) {
            index++
            path = "${pathWithoutExtension}_${decimalFormat.format(index)}.$extension"
        }
        LogDebug.d(TAG, path)
        return path
    }

    fun scanFile(savePath: String, mineTypes: Array<out String>? = null, action: (path: String, uri: Uri) -> Unit = { path: String, uri: Uri -> }) {
        MediaScannerConnection.scanFile(getApplication(), arrayOf(savePath), mineTypes) { path, uri ->
            runOnMainThread({
                action(path, uri)
            })
        }
    }

    suspend fun deleteFiles(list: Set<File>, mineTypes: Array<out String>? = null): Boolean =  withContext(Dispatchers.IO) {
            try {
                list.forEach { file ->
                    if (file.exists()) {
                        if (file.delete()) {
                            scanFile(file.absolutePath, mineTypes)
                        } else {
                            return@withContext false
                        }
                    } else {
                        return@withContext false
                    }
                }
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }

    suspend fun isVideoHaveAudioTrack(path: String): Boolean = withContext(Dispatchers.IO) {
        val audioTrack: Boolean
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(path)
        val hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)
        audioTrack = hasAudioStr != null && hasAudioStr == "yes"
        retriever.release()
        return@withContext audioTrack
    }

    fun isFileExits(path: String): Boolean {
        return File(path).exists()
    }
}
package com.dev.anhnd.mybase.utils.media

import android.content.Context
import android.database.Cursor
import java.lang.reflect.Field

fun <T : MediaModelBase> Context.getMedia(clz: Class<T>,
                                          onCheckIfAddItem: (currentList: List<T>, item: T) -> Boolean = { _, _ -> true },
                                          onCheckContinueLoad: (currentList: List<T>, item: T) -> Boolean = { _, _ -> true },
                                          projection: Array<String>? = null,
                                          selection: String? = null,
                                          selectArgs: Array<String>? = null,
                                          sortOrder: String? = null): MutableList<T> {
    val media = clz.newInstance()
    val uri = media.getUri()
    val query = contentResolver.query(
        uri,
        projection,
        selection,
        selectArgs,
        sortOrder
    )
    val data = mutableListOf<T>()
    query?.use { cursor ->
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = getRow(cursor, clz)
            if (onCheckIfAddItem(data, item)) {
                data.add(item)
            }
            if (!onCheckContinueLoad(data, item)) {
                break
            }
            cursor.moveToNext()
        }
    }
    return data
}

private fun <T : MediaModelBase> getRow(cursor: Cursor?, clz: Class<T>): T {
    val t = clz.newInstance()
    val fields = t.javaClass.declaredFields
    fields.forEach {
        it.isAccessible = true
        val annotation = it.getAnnotation(MediaInfo::class.java)
        if (annotation != null) {
            val index = cursor?.getColumnIndex(annotation.getFieldName)
            mappingField(cursor!!, index!!, it, t)
        }
    }
    return t
}

private fun <T : MediaModelBase> mappingField(cursor: Cursor,
                                              index: Int, f: Field, t: T) {
    when (f.type) {
        Int::class.java
        -> f.setInt(t, cursor.getInt(index))
        String::class.java
        -> f.set(t, cursor.getString(index))
        Float::class.java
        -> f.setFloat(t, cursor.getFloat(index))
        Long::class.java
        -> f.setLong(t, cursor.getLong(index))
    }
}

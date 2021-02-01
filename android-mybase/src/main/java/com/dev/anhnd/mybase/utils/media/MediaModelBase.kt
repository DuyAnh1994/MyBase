package com.dev.anhnd.mybase.utils.media

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

abstract class MediaModelBase {
    abstract fun getUri(): Uri
}
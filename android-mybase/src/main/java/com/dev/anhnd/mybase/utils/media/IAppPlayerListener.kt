package com.dev.anhnd.mybase.utils.media


enum class MediaState {
    READY, PLAYING, STOP, PAUSE, NOT_READY
}

interface IAppPlayerListener {
    fun onLoadComplete() {}
    fun onLoadStart() {}
    fun onVideoEnd() {}
    fun onProgressChange(progress: Long) {}
    fun onPlayerError() {}
}

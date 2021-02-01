package com.dev.anhnd.mybase.utils.view

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dev.anhnd.mybase.BaseAdapter
import com.dev.anhnd.mybase.R
import com.dev.anhnd.mybase.utils.click.onDebouncedClick
import java.math.BigDecimal

var options = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .centerCrop()

@BindingAdapter("ivLoadImageByUrl")
fun ImageView.loadBackgroundByUrl(url: String) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.default_photo)
        .apply(options.skipMemoryCache(true))
        .centerCrop()
        .into(this)
}

@BindingAdapter("ivLoadImageByUri")
fun ImageView.loadBackgroundByUrl(uri: Uri) {
    Glide.with(this)
        .load(uri)
        .apply(options.skipMemoryCache(true))
        .centerCrop()
        .into(this)
}

@BindingAdapter("deBounceClick")
fun View.onDeBoundClick(listener: View.OnClickListener?) {
    listener?.let {
        this.onDebouncedClick { _ ->
            it.onClick(this)
        }
    }
}

@BindingAdapter("rvSetFixSize")
fun RecyclerView.setFixSize(set: Boolean) {
    setHasFixedSize(set)
}

@BindingAdapter("loadBackground")
fun View.loadBackground(resId: Int) {
    setBackgroundResource(resId)
}

@BindingAdapter("rv_set_adapter")
fun <T : Any> RecyclerView.applyAdapter(applyAdapter: BaseAdapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_adapter")
fun <T : RecyclerView.ViewHolder> RecyclerView.applyAdapter(applyAdapter: RecyclerView.Adapter<T>?) {
    applyAdapter?.apply {
        adapter = applyAdapter
    }
}

@BindingAdapter("rv_set_fix_size")
fun RecyclerView.setFixSize(set: Boolean?) {
    setHasFixedSize(set ?: false)
}

fun Number.scale(numberDigitsAfterComma: Int): Number {
    return BigDecimal(this.toDouble()).setScale(numberDigitsAfterComma, BigDecimal.ROUND_HALF_EVEN)
        .toFloat()
}
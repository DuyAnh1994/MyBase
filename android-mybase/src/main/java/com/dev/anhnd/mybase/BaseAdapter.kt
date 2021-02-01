package com.dev.anhnd.mybase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseAdapter<T : Any>(
    @LayoutRes private val resLayout: Int
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    /**
     * Listener action in item of list
     */
    var listener: ListItemListener? = null

    /**
     * Current list show in screen
     */
    var data: List<T>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, resLayout, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item: T? = data?.get(holder.adapterPosition)
        /** ----- Model of list ----- */
        holder.binding.setVariable(BR.item, item)

        /** ----- callback ----- */
        holder.binding.setVariable(BR.itemListener, listener)

        /** ----- position of item in list ----- */
        holder.binding.setVariable(BR.itemPosition, holder.adapterPosition)
        holder.binding.executePendingBindings()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clearAnimation()
    }

    class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun clearAnimation() {
            binding.root.clearAnimation()
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.clearAnimation()
    }

    /**
     * All listener of adapter must implement this interface
     */
    interface ListItemListener
}

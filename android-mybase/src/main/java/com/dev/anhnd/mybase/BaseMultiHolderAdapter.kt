package com.dev.anhnd.mybase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresFeature
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dev.anhnd.mybase.BaseMultiHolderAdapter.BaseMultiHolderModel
import com.dev.anhnd.mybase.BaseMultiHolderAdapter.BaseMultiViewHolder

open class BaseMultiHolderAdapter<T : BaseMultiHolderModel>(
    @LayoutRes
    private vararg val resLayout: Int
) : RecyclerView.Adapter<BaseMultiViewHolder>() {

    companion object {
        const val LIST_EMPTY_SIZE = 0
        const val VIEW_TYPE_DEFAULT = 0
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMultiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, resLayout[viewType], parent, false)
        return BaseMultiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseMultiViewHolder, position: Int) {
        val item: T? = data?.get(holder.adapterPosition)
        holder.binding.apply {
            /** ----- Model of list ----- */
            setVariable(BR.item, item)

            /** ----- callback ----- */
            setVariable(BR.itemListener, listener)

            /** ----- position of item in list ----- */
            setVariable(BR.itemPosition, holder.adapterPosition)
            executePendingBindings()
        }
    }

    override fun getItemCount() = data?.size ?: LIST_EMPTY_SIZE

    override fun getItemViewType(position: Int) = data?.get(position)?.viewType ?: VIEW_TYPE_DEFAULT

    override fun onViewAttachedToWindow(holderMulti: BaseMultiViewHolder) {
        super.onViewAttachedToWindow(holderMulti)
        holderMulti.clearAnimation()
    }

    class BaseMultiViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun clearAnimation() {
            binding.root.clearAnimation()
        }
    }

    /**
     * Must extend this class if use multi type view holder for list
     */
    open class BaseMultiHolderModel(open val viewType: Int = VIEW_TYPE_DEFAULT)

    /**
     * All listener of adapter must implement this interface
     */
    interface ListItemListener
}

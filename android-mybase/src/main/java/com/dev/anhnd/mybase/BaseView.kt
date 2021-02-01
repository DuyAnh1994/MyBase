package com.dev.anhnd.mybase

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes

interface BaseView {

    /**
     * Layout of Activity/Fragment/Dialog
     *
     * @return R.layout.xxx
     */
    @LayoutRes
    fun getLayoutId(): Int

    /**
     * Call in onCreate to prepare data
     *
     * @param savedInstanceState
     */
    fun setup(savedInstanceState: Bundle? = null) {}

    /**
     * Setup data binding
     */
    fun initBinding() {}

    /**
     * Call before create view
     */
    fun beforeInitView() {}

    /**
     * Call after finish create view
     *
     * @param view
     * @param savedInstanceState
     */
    fun initView(view: View? = null, savedInstanceState: Bundle? = null) {}

    /**
     * Call after init view , observer data changed
     */
    fun observerViewModel() {}

    /**
     * Handle click all view in screen
     *
     * @param viewId R.id.xxx
     */
    fun onViewClick(viewId: Int) {}
}

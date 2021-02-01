package com.dev.anhnd.android_myapp

import android.os.Bundle
import android.view.View
import com.dev.anhnd.android_myapp.databinding.DialogConfirmBinding
import com.dev.anhnd.mybase.BaseDialog

class ConfirmDialog : BaseDialog<DialogConfirmBinding>() {

    override fun getLayoutId() = R.layout.dialog_confirm

    override fun getRootViewGroup() = binding.constraintRoot

    override fun getBackgroundDialog() = R.id.dialogBackground

    override fun setup(savedInstanceState: Bundle?) {

    }

    override fun initBinding() {

    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun observerViewModel() {

    }

    override fun onViewClick(viewId: Int) {

    }
}

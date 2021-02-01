package com.dev.anhnd.mybase

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dev.anhnd.mybase.utils.app.getApplication

abstract class BaseDialog<BD : ViewDataBinding> : DialogFragment(), BaseView, View.OnClickListener {

    companion object {
        private val TAG = BaseDialog::class.java.simpleName
    }

    /**
     * Binding view
     */
    protected lateinit var binding: BD

    /**
     * Listener when dismiss dialog
     */
    private val dismissListener: DialogInterface.OnDismissListener? = null
    private var needDismissOnResume = false
    private val handlerClose: Handler? = null
    private val runnableClose = Runnable {
        try {
            dismiss()
        } catch (e: IllegalStateException) {
            needDismissOnResume = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.setVariable(BR.viewListener, this)
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        beforeInitView()
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        val background: View = view.findViewById(getBackgroundDialog())
        background.setOnClickListener {
            if (isCancelOnTouchOutside()) {
                dismiss()
            }
        }
        initView(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (needDismissOnResume) {
            needDismissOnResume = false
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layout = RelativeLayout(requireActivity())
        layout.apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val dialog = object : Dialog(requireContext()) {
            override fun onBackPressed() {
                if (isDismissOnBackPressed()) {
                    dismissDialog(null)
                }
            }
        }
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(layout)
            val window = dialog.window
            window?.let { w ->
                w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val wlp = w.attributes
                if (isFullScreenWidth()) {
                    wlp.width = WindowManager.LayoutParams.MATCH_PARENT
                }
                if (isFullScreenHeight()) {
                    wlp.height = WindowManager.LayoutParams.MATCH_PARENT
                }
                if (isBottomDialog()) {
                    wlp.gravity = Gravity.BOTTOM
                }
            }
        }
        configDialog(dialog)
        return dialog
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            onViewClick(view.id)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        handlerClose?.removeCallbacks(runnableClose)
        super.onDismiss(dialog)
    }

    override fun dismiss() {
        super.dismiss()
        dismissListener?.onDismiss(dialog)
    }

    protected abstract fun getRootViewGroup(): ViewGroup

    /**
     * Must set id of parent view dialog is " backgroundDialog "
     * if you want change it, go to file item_default.xml to change
     * but always make sure the 2 ids are the same
     */
    protected abstract fun getBackgroundDialog(): Int

    /**
     * Set full width
     *
     * @return can full width
     */
    open fun isFullScreenWidth(): Boolean {
        return false
    }

    /**
     * Set full height
     *
     * @return can full height
     */
    open fun isFullScreenHeight(): Boolean {
        return false
    }

    /**
     * Set mode bottom dialog
     *
     * @return can dialog show type bottom
     */
    open fun isBottomDialog(): Boolean {
        return false
    }

    /**
     * Set cancel dialog when click button back press of device
     *
     * @return can cancel dialog when click button back press of device
     */
    open fun isDismissOnBackPressed(): Boolean {
        return true
    }

    /**
     * Set cancel dialog outside
     *
     * @return can cancel dialog when touch outside
     */
    open fun isCancelOnTouchOutside(): Boolean {
        return true
    }

    /**
     * Config information of dialog
     *
     * @param dialog my dialog
     */
    protected open fun configDialog(dialog: Dialog) {
        dialog.setCanceledOnTouchOutside(isCancelOnTouchOutside())
    }

    /**
     * Show dialog
     *
     * @param fm should use childFragmentManager
     * @param tag should pass class simple name of dialog <xxx::class.java.simpleName>
     */
    fun showDialog(fm: FragmentManager, tag: String) {
        if (!this.isAdded) {
            show(fm, tag)
        }
    }

    /**
     * Dismiss dialog
     *
     * @param tag
     */
    fun dismissDialog(tag: String?) {
        if (this.isAdded) {
            dismiss()
        }
    }

    private fun initAnimation() {
        getRootViewGroup().let {
            animateDialog(it)
        }
    }

    private fun animateDialog(viewGroup: ViewGroup) {
        if (isBottomDialog()) {
            viewGroup.startAnimation(AnimationUtils.loadAnimation(
                getApplication(),
                R.anim.anim_slide_from_bottom
            ))
        } else {
            val set = AnimatorSet()
            val animatorX = ObjectAnimator.ofFloat(viewGroup, ViewGroup.SCALE_X, 0.7f, 1f)
            val animatorY = ObjectAnimator.ofFloat(viewGroup, ViewGroup.SCALE_Y, 0.7f, 1f)
            set.playTogether(animatorX, animatorY)
            set.interpolator = BounceInterpolator()
            set.duration = 500
            set.start()
        }
    }
}

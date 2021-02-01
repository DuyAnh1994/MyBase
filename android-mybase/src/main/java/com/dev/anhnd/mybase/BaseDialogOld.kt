package com.dev.anhnd.mybase


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

@Deprecated(message = "Not support this class, migrate to BaseDialog")
abstract class BaseDialogOld<DB : ViewDataBinding> : DialogFragment(), BaseView {

    companion object {
        private val TAG = BaseDialogOld::class.java.simpleName
    }

    protected lateinit var binding: DB
    private var location: IntArray = intArrayOf(-1, -1)
    private var newGravity: Int = Gravity.NO_GRAVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        setLocation()
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        beforeInitView()
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        initAnimation()
        observerViewModel()
        dialog?.setCanceledOnTouchOutside(isCheckCancel())
    }

    override fun onResume() {
        setResolution(getDialogInfo())
        super.onResume()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissCallback()
    }

    abstract fun getDialogInfo(): DialogInfo

    protected open fun isCheckCancel(): Boolean = true

    protected open fun getRootViewGroup(): ViewGroup? = null

    protected open fun dismissCallback() {}

    private fun setResolution(dialogInfo: DialogInfo) {
        dialog?.window?.apply {
            setLayout(dialogInfo.width, dialogInfo.height)
            setBackgroundDrawableResource(dialogInfo.background)
        }
    }

    private fun setLocation() {
        val newX = location[0]
        val newY = location[1]
        if (newX != -1 && newY != -1) {
            dialog?.window?.apply {
                attributes.apply {
                    gravity = newGravity
                    x = newX
                    y = newY
                }
            }
        }
    }

    fun configLocation(location: IntArray, gravity: Int) {
        this.newGravity = gravity
        this.location = location
    }

    fun showMyDialog(fm: FragmentManager, tag: String) {
        if (!isAdded) {
            show(fm, tag)
        }
    }

    fun dismissMyDialog() {
        if (!isAdded) {
            dismiss()
        }
    }

    private fun initAnimation() {
        getRootViewGroup()?.let {
            animateDialog(it)
        }
    }

    private fun animateDialog(viewGroup: ViewGroup) {
        val set = AnimatorSet()
        val animatorX = ObjectAnimator.ofFloat(viewGroup, ViewGroup.SCALE_X, 0.7f, 1f)
        val animatorY = ObjectAnimator.ofFloat(viewGroup, ViewGroup.SCALE_Y, 0.7f, 1f)
        set.playTogether(animatorX, animatorY)
        set.interpolator = BounceInterpolator()
        set.duration = 500
        set.start()
    }
}

data class DialogInfo(
    val width: Int = -1,
    val height: Int = -1,
    val background: Int = -1
)

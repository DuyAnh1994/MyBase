package com.dev.anhnd.mybase

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.dev.anhnd.mybase.utils.log.LogDebug

abstract class BaseFragment<DB : ViewDataBinding, A : BaseActivity<*>> : Fragment(), BaseView, View.OnClickListener {

    companion object {
        private val TAG = BaseFragment::class.java.simpleName
    }

    //region Properties
    /**
     * Instance of Activity owner
     */
    @Suppress("UNCHECKED_CAST")
    val activityOwner by lazy {
        requireActivity() as A
    }

    /**
     * Binding view
     */
    protected lateinit var binding: DB

    /**
     * Manager transition fragment by Fragment Manager
     */
    protected lateinit var screenTransitionManageImp: ScreenTransitionManageImp
    private lateinit var callback: OnBackPressedCallback
    //endregion

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewListener, this)
        initBinding()
        return binding.root
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        beforeInitView()
        super.onViewCreated(view, savedInstanceState)
        if (isEnableBackPress()) {
            setBackPressedDispatcher()
        }
        screenTransitionManageImp = initScreenTransitionManager()
        initView(view, savedInstanceState)
        observerViewModel()
    }
    //endregion

    override fun beforeInitView() {
        if (getStatusBarColor() != null && isDarkText() != null) {
            setStatusColor(getStatusBarColor()!!, isDarkText()!!)
        }
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            onViewClick(view.id)
        }
    }

    /**
     * Handle button back press of device
     */
    open fun isEnableBackPress(): Boolean {
        return true
    }

    /**
     * Change color of status bar
     *
     * @return color
     */
    open fun getStatusBarColor(): Int? {
        return null
    }

    /**
     * State text color of status bar
     *
     * @return state text dark or light
     */
    open fun isDarkText(): Boolean? {
        return null
    }

    /**
     * Get layout to replace fragment if use Fragment Manager
     * Should use FrameLayout to replace fragment
     *
     * @return R.id.xxx
     */
    open fun getContainer(): Int {
        return -1
    }

    /**
     * Handle button back press of device
     */
    open fun onBackPressed() {

    }

    /**
     * Navigate to screen
     *
     * @param actionId R.id.xxx - action navigate in navigation graph
     * @param args pass bundle with navigation component
     */
    fun navigateTo(actionId: Int,
                   args: Bundle? = null) {
        try {
            with(findNavController()) {
                currentDestination?.getAction(actionId)?.let {
                    if (currentDestination?.id != currentDestination?.getAction(actionId)?.destinationId) {
                        navigate(actionId, args)
                    }
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LogDebug.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            LogDebug.e(TAG, "${e.message}")
        }
    }

    /**
     * Back to the previous screen
     */
    fun backScreen() {
        try {
            findNavController().navigateUp()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LogDebug.e(this::class.java.simpleName, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            LogDebug.e(this::class.java.simpleName, "${e.message}")
        }
    }

    /**
     * Back to one of the previous screens in the back stack
     *
     * @param actionId R.id.xxx - id of fragment want navigate to
     * @param inclusive keep back stack
     */
    fun popBackStack(actionId: Int, inclusive: Boolean = false) {
        try {
            findNavController().popBackStack(actionId, inclusive)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LogDebug.e(TAG, "${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            LogDebug.e(TAG, "${e.message}")
        }
    }

    /**
     * Back to the previous screen when use Fragment Manager
     */
    fun backScreen(tag: String) {
        val backTag = if (tag.isEmpty()) javaClass.simpleName else tag
        requireActivity().supportFragmentManager.popBackStack(
            backTag,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    private fun initScreenTransitionManager(): ScreenTransitionManageImp {
        return ScreenTransitionManageImp(requireActivity().supportFragmentManager, getContainer())
    }

    private fun setBackPressedDispatcher() {
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setStatusColor(color: Int = Color.BLACK, state: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity?.window
            window?.let { w ->
                w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                var newUiVisibility = w.decorView.systemUiVisibility
                newUiVisibility = if (state) {
                    //Dark Text to show up on your light status bar
                    newUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    //Light Text to show up on your dark status bar
                    newUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR).inv()
                }
                w.decorView.systemUiVisibility = newUiVisibility
                w.statusBarColor = color
            }
        }
    }
}

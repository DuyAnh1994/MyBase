package com.dev.anhnd.mybase.utils.bottom_navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.dev.anhnd.mybase.R

/** ----- <keep_state_fragment> is used in navigation xml ----- */
@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager, // Should pass childFragmentManager
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    var state = StateAnim.LEFT_TO_RIGHT
    private val enterAnim = R.anim.slide_exit_right_to_left
    private val exitAnim = R.anim.slide_enter_left_to_right
    private val popEnterAnim = R.anim.slide_pop_exit_left_to_right
    private val popExitAnim = R.anim.slide_pop_enter_right_to_left

    override fun navigate(destination: Destination,
                          args: Bundle?,
                          navOptions: NavOptions?,
                          navigatorExtras: Navigator.Extras?): NavDestination? {
        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()
        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            transaction.detach(currentFragment)
        } else {
            initialNavigate = true
        }
        if (state == StateAnim.LEFT_TO_RIGHT) {
            transaction.setCustomAnimations(exitAnim, enterAnim, popExitAnim, popEnterAnim)
        } else if (state == StateAnim.RIGHT_TO_LEFT) {
            transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            transaction.add(containerId, fragment, tag)
        } else {
            transaction.attach(fragment)
        }
        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()
        return if (initialNavigate) {
            destination
        } else {
            null
        }
    }
}

enum class StateAnim {
    LEFT_TO_RIGHT, RIGHT_TO_LEFT, NONE
}
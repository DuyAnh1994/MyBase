package com.dev.anhnd.mybase.utils.bottom_navigation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.ClassType
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.FragmentNavigator


@Navigator.Name("save_fragment")
class StickyFragmentNavigator(
    context: Context,
    manager: FragmentManager,
    containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun createDestination(): Destination {
        return super.createDestination()
    }

    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?): NavDestination? {
        return super.navigate(destination, args, navOptions, navigatorExtras)
    }

    override fun popBackStack(): Boolean {
        return super.popBackStack()
    }

    override fun onSaveState(): Bundle? {
        return super.onSaveState()
    }

    override fun onRestoreState(savedState: Bundle?) {
        super.onRestoreState(savedState)
    }

    override fun instantiateFragment(context: Context, fragmentManager: FragmentManager, className: String, args: Bundle?): Fragment {
        return super.instantiateFragment(context, fragmentManager, className, args)
    }
}
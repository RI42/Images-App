package com.example.myapplication.ui.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.myapplication.R

// https://github.com/google/iosched/blob/master/mobile/src/main/java/com/google/samples/apps/iosched/ui/MainNavigation.kt

/**
 * To be implemented by components that host top-level navigation destinations.
 */
interface NavigationHost {

    /** Called by MainNavigationFragment to setup it's toolbar with the navigation controller. */
    fun registerToolbarWithNavigation(toolbar: Toolbar)
}

/**
 * To be implemented by main navigation destinations shown by a [NavigationHost].
 */
interface NavigationDestination {

    /** Called by the host when the user interacts with it. */
    fun onUserInteraction() {}
}

/**
 * Fragment representing a main navigation destination. This class handles wiring up the [Toolbar]
 * navigation icon if the fragment is attached to a [NavigationHost].
 */
abstract class MainNavigationFragment : Fragment, NavigationDestination {

    constructor() : super()
    constructor(@LayoutRes res: Int) : super(res)

    protected var navigationHost: NavigationHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navigationHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // If we have a toolbar and we are attached to a proper navigation host, set up the toolbar
        // navigation icon.
        val host = navigationHost ?: return
        val mainToolbar: Toolbar = view.findViewById(R.id.toolbar) ?: return
        mainToolbar.apply {
            host.registerToolbarWithNavigation(this)
        }
    }
}

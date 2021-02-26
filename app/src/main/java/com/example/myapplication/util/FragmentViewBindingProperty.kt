package com.example.myapplication.util

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingProperty<T : ViewBinding>(
    val bind: (View) -> T
) : ReadOnlyProperty<Fragment, T> {

    internal var viewBinding: T? = null
    private val lifecycleObserver = BindingLifecycleObserver()

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        viewBinding?.let { return it }

        val view = thisRef.requireView()
        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        return bind(view).also { viewBinding = it }
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            handler.post {
                viewBinding = null
            }
        }
    }

    companion object {
        val handler = Handler(Looper.getMainLooper())
    }
}

fun <T : ViewBinding> Fragment.viewBinding(
    bind: (View) -> T
): ReadOnlyProperty<Fragment, T> {
    return FragmentViewBindingProperty(bind)
}

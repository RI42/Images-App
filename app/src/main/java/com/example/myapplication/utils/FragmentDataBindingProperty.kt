package com.example.myapplication.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentDataBindingProperty<T : ViewDataBinding>(
    private val initBlock: (T.() -> Unit)?
) : ReadOnlyProperty<Fragment, T> {

    internal var viewBinding: T? = null
    private val lifecycleObserver = BindingLifecycleObserver()

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        viewBinding?.let { return it }

        val view = thisRef.requireView()
        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        return DataBindingUtil.bind<T>(view)?.apply {
            viewBinding = this
            lifecycleOwner = thisRef.viewLifecycleOwner
            initBlock?.invoke(this)
        } ?: throw IllegalStateException(
            "view is not a root View for a layout or view hasn't been bound"
        )
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {
        val handler = Handler(Looper.getMainLooper())

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            handler.post {
                viewBinding = null
            }
        }
    }
}

inline fun <reified T : ViewDataBinding> Fragment.dataBinding(
    noinline initBlock: (T.() -> Unit)? = null
): ReadOnlyProperty<Fragment, T> {
    return FragmentDataBindingProperty(initBlock)
}

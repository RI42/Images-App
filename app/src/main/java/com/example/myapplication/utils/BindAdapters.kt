package com.example.myapplication.utils

import android.view.View
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("visibleIf")
fun View.visibleIf(visible: Boolean) {
    isVisible = visible
}

@BindingAdapter("setError")
fun TextInputLayout.setError(errorId: Int?) {
    error = errorId?.let {
        this@setError.resources.getString(it)
    }
}

@BindingAdapter("visibleIf")
fun visibleIf(view: CircularProgressIndicator, visible: Boolean) {
    if (visible) view.show() else view.hide()
}

@BindingAdapter("visibleIf")
fun visibleIf(view: LinearProgressIndicator, visible: Boolean) {
    if (visible) view.show() else view.hide()
}

@BindingAdapter("tooltipTextCompat")
fun View.tooltipTextCompat(tooltipText: String) {
    TooltipCompat.setTooltipText(this, tooltipText)
}

package com.example.myapplication.ui.utils

import kotlin.coroutines.cancellation.CancellationException

fun Throwable.checkCancellationException() {
    if (this is CancellationException) throw this
}
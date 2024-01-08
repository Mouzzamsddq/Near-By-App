package com.example.nearbyapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import com.example.nearbyapp.R

fun View.gone() {
    isGone = true
}

fun View.show() {
    isVisible = true
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.enableView(enabled: Boolean) {
    alpha = if (enabled) {
        1.0f
    } else {
        0.4f
    }
    isEnabled = enabled
}

fun View.setBackground(context: Context, drawableId: Int) {
    this.background = ContextCompat.getDrawable(
        context,
        drawableId,
    )
}

fun List<View>.show() {
    forEach {
        it.isVisible = true
    }
}

fun List<View>.hide() {
    forEach {
        it.isVisible = false
    }
}

fun Activity.hideKeyboard() =
    WindowInsetsControllerCompat(
        window,
        window.decorView,
    ).hide(WindowInsetsCompat.Type.ime())

fun View.setDominantBackground(bitmap: Bitmap?) {
    bitmap?.let {
        Palette.from(it).generate { p ->
            val color = p?.getDominantColor(ContextCompat.getColor(context, R.color.purple_main))
                ?: R.color.purple_main
            setBackgroundColor(color)
        }
    }
}

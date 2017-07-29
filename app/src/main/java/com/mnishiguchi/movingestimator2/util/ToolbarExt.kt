package com.mnishiguchi.movingestimator2.util

import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar

fun Toolbar.enableHomeAsUp(up: () -> Unit) {
    // https://developer.android.com/reference/android/support/v7/graphics/drawable/DrawerArrowDrawable.html
    navigationIcon = DrawerArrowDrawable(ctx).apply {
        progress = 1f
    }
    setNavigationOnClickListener { up() }
}

fun Toolbar.disableHomeAsUp() {
    navigationIcon = null
    setNavigationOnClickListener(null)
}

/**
 * The toolbar gets hidden when the view is scrolling down; appears when scrolling up.
 */
fun Toolbar.attachScroll(recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dy > 0) slideExit() else slideEnter()
        }
    })
}

/**
 * Restore the toolbar position and stop using scroll up/down effect.
 */
fun Toolbar.detachScroll(recyclerView: RecyclerView) {
    slideEnter()
    recyclerView.clearOnScrollListeners()
}


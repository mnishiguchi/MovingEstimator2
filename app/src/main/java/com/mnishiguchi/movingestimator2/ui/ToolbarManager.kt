package com.mnishiguchi.movingestimator2.ui

import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.util.ctx
import com.mnishiguchi.movingestimator2.util.slideEnter
import com.mnishiguchi.movingestimator2.util.slideExit
import org.jetbrains.anko.toast

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

/**
 * Specify whether it shows the up navigation action or not. Animate the toolbar when scrolling.
 * Assign the same menu to all activities and an event for the actions.
 * Adopted from https://github.com/antoniolg/Kotlin-for-Android-Developers
 */
interface ToolbarManager {

    // Abstract property declaration for a toolbar instance
    val toolbar: Toolbar

    fun initToolbar() {
        toolbar.inflateMenu(R.menu.main)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
            // R.id.action_settings -> toolbar.ctx.startActivity<SettingsActivity>()
                else -> App.instance.toast("Unknown option")
            }
            true
        }
    }
}

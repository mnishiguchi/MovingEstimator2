package com.mnishiguchi.movingestimator2.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// https://m.signalvnoise.com/using-kotlin-to-make-android-apis-fun-again-14690975afb6
// https://antonioleiva.com/extension-functions-kotlin/

/**
 * Anko provides a ctx property for activities and fragments etc, which returns the context.
 * But it does not for views. So we defines an extension function for views on our own so that we
 * can access contexts in a consistent way across the application.
 */
val View.ctx: Context
    get() = context

var TextView.textColor: Int
    get() = currentTextColor
    set(v) = setTextColor(v)

fun View.slideEnter() {
    if (translationY < 0f) animate().translationY(0f)
}

fun View.slideExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

/**
 * The line that inflates the view is the same on any adapters most of the time.
 * We might as well give ViewGroup the ability to inflate views.
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Context.inflate(layoutRes: Int, parent: ViewGroup? = null) : View {
    return LayoutInflater.from(this).inflate(layoutRes, parent, false)
}

fun View.setHeight(height: Int) {
    val params = this.layoutParams
    params.height = height
    this.layoutParams = params
}

fun View.setWedth(width: Int) {
    val params = this.layoutParams
    params.width = width
    this.layoutParams = params
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
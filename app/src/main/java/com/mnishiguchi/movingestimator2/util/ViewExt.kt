package com.mnishiguchi.movingestimator2.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

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
 * https://antonioleiva.com/extension-functions-kotlin/
 */
fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}
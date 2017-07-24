package com.mnishiguchi.movingestimator2.util

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import android.view.inputmethod.InputMethodManager

fun Activity.log(msg: String) {
    Log.d(javaClass.simpleName, msg)
}

fun Fragment.log(msg: String) {
    Log.d(javaClass.simpleName, msg)
}

fun Activity.closeSoftKeyboard() {
    // https://stackoverflow.com/a/1109108/3837223
    currentFocus?.let {
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
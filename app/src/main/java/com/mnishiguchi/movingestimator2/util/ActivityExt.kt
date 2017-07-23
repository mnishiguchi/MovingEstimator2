package com.mnishiguchi.movingestimator2.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log

fun AppCompatActivity.log(msg: String) {
    Log.d(javaClass.simpleName, msg)
}

fun Fragment.log(msg: String) {
    Log.d(javaClass.simpleName, msg)
}
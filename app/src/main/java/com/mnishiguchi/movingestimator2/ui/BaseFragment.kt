package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class BaseFragment : Fragment(), LifecycleRegistryOwner, AnkoLogger {

    // https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        info("onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        info("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        info("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        info("onResume")
        super.onResume()
    }

    override fun onPause() {
        info("onPause")
        super.onPause()
    }

    override fun onDestroy() {
        info("onDestroy")
        super.onDestroy()
    }

    override fun onAttach(context: Context?) {
        info("onAttach")
        super.onAttach(context)
    }

    override fun onDetach() {
        info("onDetach")
        super.onDetach()
    }
}

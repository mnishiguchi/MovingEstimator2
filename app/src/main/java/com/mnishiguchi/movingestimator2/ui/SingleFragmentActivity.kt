package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.mnishiguchi.movingestimator2.R

/**
 * A generic activity superclass for hosting a single fragment.
 * We need to subclass a subclass of android.support.v4.app.FragmentActivity, so that we can use:
 *   + support-library fragments
 *   + cross-api-version toolbar
 * https://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html
 */
abstract class SingleFragmentActivity : AppCompatActivity(), LifecycleRegistryOwner {

    // https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    /**
     * A subclass can choose to override this function to set a different layout.
     */
    @LayoutRes
    protected open fun getLayoutResId(): Int = R.layout.activity_fragment

    /**
     * Subclasses of [SingleFragmentActivity] must implement this method.
     * @return An instance of the fragment that the activity is hosting.
     */
    abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        // Use supportFragmentManager because we are using the support library fragments.
        val fm = supportFragmentManager

        // Register a fragment to the fragment manager if not already.
        // NOTE: Never add the same fragment twice!
        if (findFragment(fm) == null) registerFragment(fm)
    }

        // A container view ID serves two purposes:
        // 1. Tells the FragmentManager where in the activity's view the fragment's view should appear.
        // 2. Used as a unique identifier for a fragment in the FragmentManager's list.

    // Find a fragment instance in a fragment manager's list.
    private fun findFragment(fm: FragmentManager): Fragment? {
        return fm.findFragmentById(R.id.fragment_container)
    }

    // Create a new fragment instance and register it to a fragment manager.
    private fun registerFragment(fm: FragmentManager) {
        fm.beginTransaction()
                .add(R.id.fragment_container, createFragment())
                .commit()
    }
}


package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.util.log
import org.jetbrains.anko.*

/**
 * A generic activity superclass for hosting a single fragment.
 * We need to subclass a subclass of android.support.v4.app.FragmentActivity, so that we can use:
 *   + support-library fragments
 *   + cross-api-version toolbar
 * https://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html
 */
abstract class BaseActivity : AppCompatActivity(), LifecycleRegistryOwner, AnkoLogger {

    // https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    /**
     * A subclass can choose to override this function to set a different layout.
     */
    @LayoutRes
    protected open fun getLayoutResId(): Int = R.layout.activity_fragment

    /**
     * Subclasses of [BaseActivity] must implement this method.
     * @return An instance of the fragment that the activity is hosting.
     */
    abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        info("onCreate")

        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

        // Set our custom toolbar as the Activity's action bar.
        // https://developer.android.com/reference/android/support/v7/widget/Toolbar.html
        // https://developer.android.com/reference/android/support/v7/app/AppCompatActivity.html#setSupportActionBar(android.support.v7.widget.Toolbar)
        setSupportActionBar(find<Toolbar>(R.id.toolbar))

        // Use supportFragmentManager because we are using the support library fragments.
        val fm = supportFragmentManager

        // Register a fragment to the fragment manager if not already.
        // NOTE: Never add the same fragment twice!
        if (fm.findFragmentById(R.id.fragment_container) == null) {
            fm.beginTransaction()
                    .add(R.id.fragment_container, createFragment())
                    .commit()
        }

        // A container view ID serves two purposes:
        // 1. Tells the FragmentManager where in the activity's view the fragment's view should appear.
        // 2. Used as a unique identifier for a fragment in the FragmentManager's list.
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

    // Create common menu items.
    // https://developer.android.com/guide/topics/ui/menus.html#options-menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // https://developer.android.com/guide/topics/ui/menus.html#RespondingOptionsMenu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_settings -> {
                // TODO - Start the settings activity
                // toolbar.ctx.startActivity<SettingsActivity>()
                ctx.toast("menu_item_settings")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}


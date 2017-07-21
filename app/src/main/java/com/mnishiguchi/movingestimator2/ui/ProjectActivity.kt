package com.mnishiguchi.movingestimator2.ui

import android.os.Bundle
import android.support.v4.app.Fragment

class ProjectActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return ProjectListFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

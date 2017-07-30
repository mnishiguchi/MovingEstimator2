package com.mnishiguchi.movingestimator2.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project

class ProjectActivity : SingleFragmentActivity(), ProjectListFragment.OnInteractionListener {

    override fun createFragment(): Fragment {
        return ProjectListFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onListItemSelected(project: Project) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ProjectFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }
}

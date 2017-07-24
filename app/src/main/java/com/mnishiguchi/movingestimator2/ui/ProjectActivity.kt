package com.mnishiguchi.movingestimator2.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.log
import org.jetbrains.anko.find

class ProjectActivity : SingleFragmentActivity(), ProjectListFragment.OnInteractionListener, ToolbarManager {

    override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

    override fun createFragment(): Fragment {
        return ProjectListFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        toolbar.title = "Projects"
    }

    override fun onListItemSelected(project: Project) {
        log("onListItemSelected: $project")

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ProjectDetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }
}

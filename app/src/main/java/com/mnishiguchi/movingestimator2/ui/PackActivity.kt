package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.mnishiguchi.movingestimator2.data.Pack
import com.mnishiguchi.movingestimator2.viewmodel.PackVM
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class PackActivity : BaseActivity(), PackListFragment.OnInteractionListener {

    companion object {
        private val EXTRA_PROJECT_ID = "${PackActivity::class.java.canonicalName}.EXTRA_PROJECT_ID"

        // Define an extra intent for starting this activity.
        fun newIntent(packageContext: Context, projectId: Int): Intent {
            return Intent(packageContext, PackActivity::class.java).apply {
                putExtra(EXTRA_PROJECT_ID, projectId)
            }
        }
    }

    override fun createFragment(): Fragment {
        return PackListFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var projectId = intent.getSerializableExtra(EXTRA_PROJECT_ID) as Int

        ViewModelProviders.of(this).get(PackVM::class.java).apply {
            init(projectId)
            info("initialize PackVM: projectId: $projectId")
        }

        super.onCreate(savedInstanceState)
    }

    override fun onListItemSelected(pack: Pack) {
        toast(pack.id)
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, PackFragment.newInstance())
//                .addToBackStack(null)
//                .commit()
    }
}

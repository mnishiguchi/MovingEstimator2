package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.closeSoftKeyboard
import com.mnishiguchi.movingestimator2.util.log
import com.mnishiguchi.movingestimator2.viewmodel.ProjectVM
import kotlinx.android.synthetic.main.fragment_project_detail.*
import kotlinx.android.synthetic.main.toolbar.*


class ProjectDetailFragment : Fragment(), LifecycleRegistryOwner {

    // https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    companion object {
        fun newInstance(): ProjectDetailFragment {
            return ProjectDetailFragment()
        }
    }

    private val vm: ProjectVM by lazy { ViewModelProviders.of(activity).get(ProjectVM::class.java) }

    private lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get a copy of the selected project. Assume that the selected project exists.
        project = vm.selectedProject().value ?: throw RuntimeException("selectedProject does not exist")
    }

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_project_detail, container, false)
    }

    // Set up sub-views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(projectDetailName) {
            setText(project.name)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    project.name = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    vm.update(project)
                }
            })
        }

        with(projectDetailDescription) {
            setText(project.description)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    project.description = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    vm.update(project)
                }
            })
        }

        with(projectDetailDate) {
            setText(project.moveDate.toString())
        }
    }

    override fun onResume() {
        log("onResume")
        super.onResume()

        with(activity.toolbar) {
            title = project.name
            subtitle = project.moveDate.toString()
            enableHomeAsUp { activity.onBackPressed() }
        }
    }

    override fun onPause() {
        log("onPause")
        super.onPause()

        activity.closeSoftKeyboard()
    }
}

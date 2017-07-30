package com.mnishiguchi.movingestimator2.ui

import android.app.Activity
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.closeSoftKeyboard
import com.mnishiguchi.movingestimator2.util.enableHomeAsUp
import com.mnishiguchi.movingestimator2.util.log
import com.mnishiguchi.movingestimator2.viewmodel.ProjectVM
import kotlinx.android.synthetic.main.fragment_project_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.util.*

class ProjectFragment : Fragment(), LifecycleRegistryOwner {

    // https://developer.android.com/reference/android/arch/lifecycle/LifecycleRegistryOwner.html
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry = lifecycleRegistry

    companion object {
        val DIALOG_DATE = "DIALOG_DATE"
        val REQUEST_DATE = 0

        fun newInstance(): ProjectFragment {
            return ProjectFragment()
        }
    }

    private val vm: ProjectVM by lazy { ViewModelProviders.of(activity).get(ProjectVM::class.java) }

    private lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tell the FragmentManager that this fragment need its onCreateOptionsMenu to be called.
        setHasOptionsMenu(true)

        // Get a copy of the selected project. Assume that the selected project exists.
        project = vm.selectedProject().value ?: throw IllegalArgumentException("project must be present")
    }

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_project_detail, container, false)
    }

    // Set up sub-views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.toolbar.apply {
            title = project.name
            enableHomeAsUp { activity.onBackPressed() }
        }

        projectDetailName.apply {
            setText(project.name) // Editable
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    project.name = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    vm.update(project)
                    activity.toolbar.title = project.name
                }
            })
        }

        projectDetailDescription.apply {
            setText(project.description) // Editable
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

        projectDetailDate.apply {
            setOnClickListener { startDatePickerForResult() }
        }

        updateDateUI()
    }

    override fun onResume() {
        log("onResume")
        super.onResume()
    }

    override fun onPause() {
        log("onPause")
        super.onPause()

        activity.closeSoftKeyboard()
    }

    // Create menu items for this fragment.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_project_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_project_detail_add_package -> {
                toast("menu_item_project_detail_add_package")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            log("Result was non-OK")
            return
        }

        when (requestCode) {
            REQUEST_DATE -> {
                data?.let {
                    project.moveDate = DatePickerFragment.dateResult(data).time // In-memory
                    vm.update(project) // DB
                    updateDateUI() // UI
                }
            }
        }
    }

    private fun startDatePickerForResult() {
        val dialog = DatePickerFragment.newInstance(Date(project.moveDate))
        dialog.setTargetFragment(this, REQUEST_DATE)
        dialog.show(activity.supportFragmentManager, DIALOG_DATE)
    }

    /**
     * Update the date text.
     */
    private fun updateDateUI() {
        val stringResId = if (project.moveDate > Date().time) R.string.moving_on else R.string.moved_on
        val dateString = ctx.getString(stringResId, App.longDateFormat.format(project.moveDate))

        projectDetailDate.text = dateString
        activity.toolbar.subtitle = dateString
    }
}
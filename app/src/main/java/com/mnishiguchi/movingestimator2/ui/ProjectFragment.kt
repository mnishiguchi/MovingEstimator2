package com.mnishiguchi.movingestimator2.ui

import android.app.Activity
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.closeSoftKeyboard
import com.mnishiguchi.movingestimator2.util.ctx
import com.mnishiguchi.movingestimator2.util.enableHomeAsUp
import com.mnishiguchi.movingestimator2.util.log
import com.mnishiguchi.movingestimator2.viewmodel.ProjectVM
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.util.*

class ProjectFragment : BaseFragment() {

    companion object {
        val DIALOG_DATE = "DIALOG_DATE"
        val REQUEST_DATE = 0

        fun newInstance(): ProjectFragment {
            return ProjectFragment()
        }
    }

    private val vm: ProjectVM by lazy { ViewModelProviders.of(activity).get(ProjectVM::class.java) }

    // A ref to a project object, initially an empty object.
    private var project: Project = Project()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tell the FragmentManager that this fragment need its onCreateOptionsMenu to be called.
        setHasOptionsMenu(true)
    }

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    // Set up sub-views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectName.apply {
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

        projectDescription.apply {
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

        projectDate.apply {
            setOnClickListener { startDatePickerForResult() }
        }

        projectPacks.apply {
            setOnClickListener {
                info("project.id: ${project.id}")
                activity.startActivity(PackActivity.newIntent(ctx, project.id))
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Update the UI when the data is updated.
        vm.selectedProject().observe(this as LifecycleOwner, Observer<Project> {
            it?.let {
                info("data provided: $it")
                project = it
                updateUI()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        activity.toolbar.apply {
            title = project.name
            enableHomeAsUp { activity.onBackPressed() }
        }
    }

    override fun onPause() {
        super.onPause()

        activity.closeSoftKeyboard()
    }

    // Create menu items for this fragment.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_project, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_project_add_package -> {
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
                    updateUI() // UI
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
    private fun updateUI() {
        val stringResId = if (project.moveDate > Date().time) R.string.moving_on else R.string.moved_on
        val dateString = ctx.getString(stringResId, App.longDateFormat.format(project.moveDate))

        projectName.setText(project.name) // Editable
        projectDescription.setText(project.description) // Editable
        projectDate.text = dateString
        activity.toolbar.subtitle = dateString
    }
}

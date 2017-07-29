package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.*
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.*
import com.mnishiguchi.movingestimator2.viewmodel.ProjectVM
import kotlinx.android.synthetic.main.fragment_project_list.*
import kotlinx.android.synthetic.main.list_item_project.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.util.*

class ProjectListFragment : Fragment() {
    companion object {
        fun newInstance(): ProjectListFragment {
            return ProjectListFragment()
        }
    }

    // Container Activity must implement this interface
    // https://developer.android.com/training/basics/fragments/communicating.html
    interface OnInteractionListener {
        fun onListItemSelected(project: Project)
    }

    private var callback: OnInteractionListener? = null

    private val vm: ProjectVM by lazy { ViewModelProviders.of(activity).get(ProjectVM::class.java) }

    private lateinit var adapter: ProjectListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tell the FragmentManager that this fragment need its onCreateOptionsMenu to be called.
        setHasOptionsMenu(true)
    }

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_project_list, container, false)
    }

    // Set up sub-views
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.adapter = ProjectListAdapter(
                projects = emptyList<Project>(),
                itemClick = { selectProject(it) },
                menuItemClick = { actionType, payload -> selectMenuAction(actionType, payload) }
        )

        projectList.adapter = this.adapter
        projectList.layoutManager = LinearLayoutManager(activity)

        fab.setOnClickListener {
            vm.create()
            projectList.layoutManager.scrollToPosition(0)
        }

        // Update the adapter's data whenever data set is changed.
        vm.projects.observe(activity as LifecycleOwner, Observer<List<Project>> {
            it?.let {
                this.adapter.replaceDataSet(it)
                setListVisibility()
            }
        })
    }

    override fun onResume() {
        log("onResume")
        super.onResume()

        activity.toolbar.apply {
            title = ctx.getString(R.string.toolbar_title_projects)
            subtitle = ""
            disableHomeAsUp()
            attachScroll(projectList)
        }
    }

    override fun onPause() {
        log("onPause")
        super.onPause()

        activity.toolbar.apply {
            detachScroll(projectList)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnInteractionListener) {
            callback = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    // Create menu items for this fragment.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_project_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_project_list_sort -> {
                // TODO - Sort the list
                ctx.toast("menu_item_project_list_sort")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun selectProject(project: Project) {
        callback?.onListItemSelected(project)
        vm.selectProject(project)
    }

    private fun selectMenuAction(actionType: ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE, payload: Any) {
        when (actionType) {
            ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE.SHOW -> {
                val project = payload as Project // Decode payload
                selectProject(project)
            }
            ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE.DELETE -> {
                val project = payload as Project // Decode payload
                val name = if (project.name.isBlank()) "a project" else project.name
                AlertDialog.Builder(ctx)
                        .setTitle("Deleting $name (id: ${project.id})")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes") { _, _ -> vm.destroy(project) { toast("Deleted $name") } }
                        .setNegativeButton("No") { _, _ -> }
                        .show()
            }
        }
    }

    /**
     * Show the placeholder view if the list is empty.
     */
    private fun setListVisibility() {
        // java.lang.NullPointerException: Attempt to invoke virtual method 'void android.support.v7.widget.RecyclerView.setVisibility(int)' on a null object reference
        if (projectList == null || emptyList == null) return

        if (vm.isEmpty()) {
            projectList.visibility = View.GONE
            emptyList.visibility = View.VISIBLE
        } else {
            projectList.visibility = View.VISIBLE
            emptyList.visibility = View.GONE
        }
    }

    /**
     * A list adapter for ProjectListFragment.
     */
    class ProjectListAdapter(private var projects: List<Project> = emptyList(),
                             val itemClick: (project: Project) -> Unit,
                             val menuItemClick: (actionType: ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE, payload: Any) -> Unit
    ) : RecyclerView.Adapter<ProjectListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = parent.inflate(R.layout.list_item_project)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(projects[position], itemClick, menuItemClick)
        }

        override fun getItemCount(): Int = projects.size

        fun replaceDataSet(projects: List<Project>) {
            this.projects = projects
            notifyDataSetChanged()
        }

        /**
         * A view holder for ProjectListAdapter.
         * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html
         */
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            enum class MENU_ACTION_TYPE { SHOW, DELETE }

            fun bind(project: Project,
                     itemClick: (project: Project) -> Unit,
                     menuItemClick: (actionType: ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE, payload: Any) -> Unit
            ) = with(itemView) {
                listItemProjectCard.setOnClickListener { itemClick(project) }

                listItemProjectName.apply {
                    text = if (project.name.isBlank())
                        App.instance.getString(android.R.string.untitled)
                    else
                        project.name
                }

                listItemProjectDescription.apply {
                    text = project.description
                    visibility = if (project.description.isBlank()) View.GONE else View.VISIBLE
                }

                listItemProjectMoveDate.apply {
                    val stringResId = if (project.moveDate > Date().time) R.string.moving_on else R.string.moved_on
                    text = ctx.getString(stringResId, App.mediumDateFormat.format(project.moveDate))
                }

                listItemProjectPopupTrigger.setOnClickListener {
                    PopupMenu(context, listItemProjectPopupTrigger, Gravity.RIGHT).apply {
                        inflate(R.menu.list_item_project)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.menu_item_project_list_popup_show -> menuItemClick(MENU_ACTION_TYPE.SHOW, project)
                                R.id.menu_item_project_list_popup_delete -> menuItemClick(MENU_ACTION_TYPE.DELETE, project)
                            }
                            false
                        }
                        show()
                    }
                }
            }
        }
    }
}

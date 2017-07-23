package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.inflate
import com.mnishiguchi.movingestimator2.viewmodel.ProjectVM
import kotlinx.android.synthetic.main.fragment_project_list.*
import kotlinx.android.synthetic.main.list_item_project.view.*
import org.jetbrains.anko.support.v4.toast

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
                itemClick = { this.onItemClick(it) },
                menuItemClick = { actionType, payload -> this.onMenuItemClick(actionType, payload) }
        )

        projectList.adapter = this.adapter
        projectList.layoutManager = LinearLayoutManager(activity)

        fab.setOnClickListener { toast("fab clicked") }

        // Update the adapter's data whenever data set is changed.
        vm.projects.observe(activity as LifecycleOwner, Observer<List<Project>> {
            it?.let {
                this.adapter.replaceDataSet(it)
            }
        })
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

    private fun onItemClick(project: Project) {
        toast("Card ${project.id} clicked")
        callback?.onListItemSelected(project)
        vm.selectProject(project)
    }

    private fun onMenuItemClick(actionType: ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE, payload: Any) {
        when (actionType) {
            ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE.SHOW -> {
                val project = payload as Project // Decode payload
                toast("Menu item clicked: show ${project.id}")
            }
            ProjectListAdapter.ViewHolder.MENU_ACTION_TYPE.DELETE -> {
                val project = payload as Project // Decode payload
                toast("Menu item clicked: delete ${project.id}")
            }
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
                listItemProjectName.setText(project.name)
                listItemProjectDescription.setText(project.description)
                listItemProjectMoveDate.setText(project.moveDate.toString())
                listItemProjectPopupTrigger.setOnClickListener {
                    PopupMenu(context, listItemProjectPopupTrigger, Gravity.RIGHT).apply {
                        inflate(R.menu.list_item_project)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.menu_item_list_item_project_add -> menuItemClick(MENU_ACTION_TYPE.SHOW, project)
                                R.id.menu_item_list_item_project_delete -> menuItemClick(MENU_ACTION_TYPE.DELETE, project)
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

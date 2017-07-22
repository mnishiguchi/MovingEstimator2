package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
    private val TAG = javaClass.simpleName

    companion object {
        fun newInstance(): ProjectListFragment {
            return ProjectListFragment()
        }
    }

    private val vm: ProjectVM by lazy { ViewModelProviders.of(activity).get(ProjectVM::class.java) }
    private lateinit var adapter: ProjectListAdapter
    // private var listener: OnFragmentInteractionListener? = null

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
                menuItemClick = { type, item -> this.onMenuItemClick(type, item) }
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

    fun onItemClick(project: Project) {
        toast("Card ${project.id} clicked")
    }

    fun onMenuItemClick(type: String, project: Project) {
        when (type) {
            ProjectListAdapter.ViewHolder.TYPE_SHOW -> toast("Menu item clicked: show ${project.id}")
            ProjectListAdapter.ViewHolder.TYPE_DELETE -> toast("Menu item clicked: delete ${project.id}")
        }
    }

//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        listener = null
//    }

    /**
     * A list adapter for ProjectListFragment.
     */
    class ProjectListAdapter(private var projects: List<Project> = emptyList(),
                             val itemClick: (project: Project) -> Unit,
                             val menuItemClick: (type: String, project: Project) -> Unit
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
            companion object {
                val TYPE_SHOW = "TYPE_SHOW"
                val TYPE_DELETE = "TYPE_DELETE"
            }

            fun bind(project: Project,
                     itemClick: (project: Project) -> Unit,
                     menuItemClick: (type: String, project: Project) -> Unit
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
                                R.id.menu_item_list_item_project_add -> menuItemClick(TYPE_SHOW, project)
                                R.id.menu_item_list_item_project_delete -> menuItemClick(TYPE_DELETE, project)
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

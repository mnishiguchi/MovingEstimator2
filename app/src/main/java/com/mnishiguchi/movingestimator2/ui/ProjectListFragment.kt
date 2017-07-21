package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

        projectList.adapter = ProjectListAdapter()
        projectList.layoutManager = LinearLayoutManager(activity)

        fab.setOnClickListener { toast("fab clicked") }

        updateUI()
    }

    fun updateUI() {
        vm.projects.observe(activity as LifecycleOwner, Observer<List<Project>> {
            it?.let {
                projectList.adapter = ProjectListAdapter(it)
            }
        })
    }

//    override fun onResume() {
//        super.onResume()
//
//        // Refresh the state of the +1 button each time the activity receives focus.
//        // mPlusOneButton!!.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE)
//    }
//
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

    class ProjectListAdapter(var projects: List<Project> = emptyList()) : RecyclerView.Adapter<ProjectListAdapter.ViewHolder>() {
        private val TAG = javaClass.simpleName

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = parent.inflate(R.layout.list_item_project)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(projects[position])
        }

        override fun getItemCount(): Int = projects.size

        /**
         * A view holder for ProjectListAdapter.
         * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html
         */
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(project: Project) = with(itemView) {
                listItemProjectName.setText(project.name)
                listItemProjectDescription.setText(project.description)
                listItemProjectMoveDate.setText(project.moveDate.toString())
            }
        }
    }
}

package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Pack
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.util.ctx
import com.mnishiguchi.movingestimator2.util.inflate
import com.mnishiguchi.movingestimator2.util.log
import com.mnishiguchi.movingestimator2.viewmodel.ProjectVM
import kotlinx.android.synthetic.main.fragment_pack_list.*
import kotlinx.android.synthetic.main.list_item_pack.view.*

/**
 * A fragment representing a list of Pack.
 */
class PackListFragment : Fragment() {
    companion object {
        fun newInstance(): PackListFragment {
            return PackListFragment()
        }
    }

    private val vm: ProjectVM by lazy { ViewModelProviders.of(activity).get(ProjectVM::class.java) }

    private lateinit var adapter: PackListAdapter
    private lateinit var project: Project

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate")

        // Get a copy of the selected project. Assume that the selected project exists.
        project = vm.selectedProject().value ?: throw IllegalArgumentException("project must be present")
    }

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pack_list, container, false)
    }

    // Set up sub-views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        log("onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView with no data
        this.adapter = PackListAdapter()
        packList.adapter = this.adapter
        packList.layoutManager = LinearLayoutManager(activity)

        // Observe packs and update the list when the data is updated.
        vm.packs(project.id).observe(activity as LifecycleOwner, Observer<List<Pack>> {
            it?.let {
                // FIXME - Right now, the data goes empty on configuration change.
                log("data provided: $it")
                this.adapter.replaceDataSet(it)
            }
        })

        DividerItemDecoration(packList.ctx, LinearLayoutManager(activity).orientation).apply {
            packList.addItemDecoration(this)
        }
    }

    override fun onResume() {
        log("onResume")
        super.onResume()
    }

    override fun onPause() {
        log("onPause")
        super.onPause()
    }

    override fun onDestroy() {
        log("onDestroy()")
        super.onDestroy()
    }

    /**
     * A list adapter for PackListFragment.
     */
    class PackListAdapter(private var packs: List<Pack> = emptyList()) : RecyclerView.Adapter<PackListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackListAdapter.ViewHolder {
            val view = parent.inflate(R.layout.list_item_pack)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(packs[position])
        }

        override fun getItemCount(): Int = packs.size

        fun replaceDataSet(packs: List<Pack>) {
            this.packs = packs
            notifyDataSetChanged()
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(pack: Pack) = with(itemView) {
                listItemPackName.text = pack.name
            }
        }
    }
}

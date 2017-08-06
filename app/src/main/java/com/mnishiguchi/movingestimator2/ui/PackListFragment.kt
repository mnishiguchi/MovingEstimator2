package com.mnishiguchi.movingestimator2.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnishiguchi.movingestimator2.R
import com.mnishiguchi.movingestimator2.data.Pack
import com.mnishiguchi.movingestimator2.util.*
import com.mnishiguchi.movingestimator2.viewmodel.PackVM
import io.bloco.faker.Faker
import kotlinx.android.synthetic.main.fragment_pack_list.*
import kotlinx.android.synthetic.main.list_item_pack.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 * A fragment representing a list of Pack.
 */
class PackListFragment : BaseFragment() {

    companion object {
        fun newInstance(): PackListFragment = PackListFragment()
    }

    // Container Activity must implement this interface
    // https://developer.android.com/training/basics/fragments/communicating.html
    interface OnInteractionListener {
        fun onListItemSelected(pack: Pack)
    }

    private var callback: OnInteractionListener? = null

    private lateinit var adapter: PackListAdapter

    private val vm: PackVM by lazy { ViewModelProviders.of(activity).get(PackVM::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tell the FragmentManager that this fragment need its onCreateOptionsMenu to be called.
        setHasOptionsMenu(true)
    }

    // Inflate the layout for this fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pack_list, container, false)
    }

    // Set up sub-views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        // TODO - Add click listeners
        this.adapter = PackListAdapter(packs = emptyList())
        packList.adapter = this.adapter
        packList.layoutManager = LinearLayoutManager(activity)

        DividerItemDecoration(packList.ctx, LinearLayoutManager(activity).orientation).apply {
            packList.addItemDecoration(this)
        }

        fab.setOnClickListener {
            // TODO - Create a record
            // vm.create()
            toast("fab clicked")

            packList.layoutManager.scrollToPosition(0)
        }

        // Create fake data.
        doAsync {
            val faker = Faker()
            val fakePacks = listOf(
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence()),
                    Pack(name = faker.commerce.productName(), description = faker.lorem.sentence())
            )
            uiThread {
                this@PackListFragment.adapter.replaceDataSet(fakePacks)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        // Update the UI when the data is updated.
//        val packsChangedObserver = Observer<List<Pack>> {
//            it?.let {
//                info("data provided: $it")
//
//                this.adapter.replaceDataSet(it)
//                setListVisibility()
//            }
//        }
//        vm.getPacks().observe(this as LifecycleOwner, packsChangedObserver)
    }

    override fun onResume() {
        super.onResume()

        activity.toolbar.apply {
            title = ctx.getString(R.string.project_packs)
            subtitle = ""
            enableHomeAsUp { activity.onBackPressed() }
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is PackListFragment.OnInteractionListener) {
            callback = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    /**
     * Show the placeholder view if the list is empty.
     */
    private fun setListVisibility() {
        // java.lang.NullPointerException: Attempt to invoke virtual method 'void android.support.v7.widget.RecyclerView.setVisibility(int)' on a null object reference
        if (packList == null || emptyList == null) return

        if (vm.isEmpty()) {
            packList.gone()
            emptyList.visible()
        } else {
            packList.visible()
            emptyList.gone()
        }
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
                listItemPackSize.text = pack.volume.toString()
                listItemPackQuantity.text = pack.quantity.toString()
                listItemPackRoom.text = ""
                listItemPackComment.text = pack.description
            }
        }
    }
}

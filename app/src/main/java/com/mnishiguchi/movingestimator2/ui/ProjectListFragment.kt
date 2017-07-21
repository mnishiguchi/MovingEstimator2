package com.mnishiguchi.movingestimator2.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnishiguchi.movingestimator2.R
import kotlinx.android.synthetic.main.fragment_project.*
import org.jetbrains.anko.support.v4.toast

class ProjectListFragment : Fragment() {
    private val TAG = javaClass.simpleName

    companion object {
        fun newInstance(): ProjectListFragment {
            return ProjectListFragment()
        }
    }

    // private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener { toast("fab clicked") }
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
}

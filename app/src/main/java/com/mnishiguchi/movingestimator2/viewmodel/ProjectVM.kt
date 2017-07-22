package com.mnishiguchi.movingestimator2.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mnishiguchi.movingestimator2.data.Project

/**
 * A ViewModel for ProjectActivity.
 * https://developer.android.com/topic/libraries/architecture/viewmodel.html
 */
class ProjectVM : ViewModel() {
    /* ==> Data store */

    // val dao: ProjectDao = App.database.projectDao()
    // val projects: LiveData<List<Project>> = dao.all()

    // A temp fake data set
    val projects: LiveData<List<Project>> = MutableLiveData<List<Project>>().apply {
        value = listOf(
                Project(id = 1, name = "Project", description = "Description"),
                Project(id = 2, name = "Project", description = "Description"),
                Project(id = 3, name = "Project", description = "Description"),
                Project(id = 4, name = "Project", description = "Description"),
                Project(id = 5, name = "Project", description = "Description"),
                Project(id = 6, name = "Project", description = "Description")
        )
    }

    /* ==> Selected item */

    /* ==> Read operations */

    /* ==> Write operations */

}
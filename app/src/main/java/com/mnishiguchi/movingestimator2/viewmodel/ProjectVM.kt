package com.mnishiguchi.movingestimator2.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.data.ProjectDao
import io.bloco.faker.Faker
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A ViewModel for ProjectActivity.
 * https://developer.android.com/topic/libraries/architecture/viewmodel.html
 */
class ProjectVM(val projectDao: ProjectDao = App.database.projectDao()) : ViewModel() {

    // Insert a fake data set into database
    private fun insertFakeProjects() {
        val faker = Faker() // https://github.com/blocoio/faker

        for (i in 1..6) {
            insert(Project(id = i, name = faker.address.country(), description = faker.lorem.paragraph()))
        }
    }

//    // Uncomment only when we need to seed the database because this is an expensive operation.
//    init {
//        insertFakeProjects()
//    }

    /* ==> Data store */

    private val projects: LiveData<List<Project>> = projectDao.all()
    private val currentProject = MutableLiveData<Project>()

    /* projects */

    fun getProjects() = projects

    /* ==> currentProject */

    fun selectProject(project: Project) {
        currentProject.value = project
    }

    fun selectedProject(): LiveData<Project> = currentProject

    /* ==> CRUD operations */

    fun isEmpty(): Boolean = projects.value?.isEmpty() ?: true

    fun create(): Project = Project().apply {
        val project = this
        doAsync { projectDao.insert(project) }
    }

    fun create(onCreate: (id: Int) -> Unit): Project = Project().apply {
        val project = this
        doAsync {
            val id: Long = projectDao.insert(project)
            uiThread { onCreate(id.toInt()) }
        }
    }

    fun insert(project: Project) {
        doAsync { projectDao.insert(project) }
    }

    fun update(project: Project) {
        insert(project)
    }

    fun destroy(project: Project) {
        doAsync { projectDao.delete(project) }
    }

    fun destroy(project: Project, onDestroy: (Int) -> Unit) {
        doAsync {
            val count = projectDao.delete(project)
            uiThread { onDestroy(count) }
        }
    }

    fun destroy(id: Int) {
        doAsync { projectDao.delete(id) }
    }

    fun destroy(id: Int, onDestroy: (Int) -> Unit) {
        doAsync {
            val count = projectDao.delete(id)
            uiThread { onDestroy(count) }
        }
    }
}
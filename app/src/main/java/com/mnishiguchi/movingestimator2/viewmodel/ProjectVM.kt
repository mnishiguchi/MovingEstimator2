package com.mnishiguchi.movingestimator2.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.data.Pack
import com.mnishiguchi.movingestimator2.data.Project
import com.mnishiguchi.movingestimator2.data.ProjectDao
import io.bloco.faker.Faker
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * A ViewModel for ProjectActivity.
 * https://developer.android.com/topic/libraries/architecture/viewmodel.html
 */
class ProjectVM(val dao: ProjectDao = App.database.projectDao()) : ViewModel() {
    /* ==> Data store */

    val projects: LiveData<List<Project>> = dao.all()

    private val currentProject = MutableLiveData<Project>()

    init {
        // Insert a fake data set into database
        // https://github.com/blocoio/faker
        val faker = Faker()

        // Fake projects
        for (i in 1..6) {
            insert(Project(id = i, name = faker.address.country(), description = faker.lorem.paragraph()))
        }

        // Fake packs
        for (i in 1..6) {
            for (j in 1..6) {
                insert(Pack(projectId = i, name = faker.commerce.productName()))
            }
        }
    }

    /* ==> currentProject */

    fun selectProject(project: Project) {
        currentProject.value = project
    }

    fun selectedProject(): LiveData<Project> = currentProject

    /* ==> projects */

    fun isEmpty(): Boolean = projects.value?.isEmpty() ?: true

    fun create(): Project = Project().apply {
        val project = this
        doAsync { dao.insert(project) }
    }

    fun create(onCreate: (id: Int) -> Unit): Project = Project().apply {
        val project = this
        doAsync {
            val id: Long = dao.insert(project)
            uiThread { onCreate(id.toInt()) }
        }
    }

    fun insert(project: Project) {
        doAsync { dao.insert(project) }
    }

    fun insert(pack: Pack) {
        doAsync { dao.insertPack(pack) }
    }

    fun update(project: Project) {
        insert(project)
    }

    fun destroy(project:Project) {
        doAsync { dao.delete(project) }
    }

    fun destroy(project:Project, onDestroy: (Int) -> Unit) {
        doAsync {
            val count = dao.delete(project)
            uiThread { onDestroy(count) }
        }
    }
    fun destroy(id: Int) {
        doAsync { dao.delete(id) }
    }

    fun destroy(id: Int, onDestroy: (Int) -> Unit) {
        doAsync {
            val count = dao.delete(id)
            uiThread { onDestroy(count) }
        }
    }

    /* ==> packs */

    fun packs(projectId: Int): LiveData<List<Pack>> = dao.packs(projectId)
}
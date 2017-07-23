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
class ProjectVM(val dao: ProjectDao = App.database.projectDao()) : ViewModel() {
    /* ==> Data store */

    val projects: LiveData<List<Project>> = dao.all()

    private val currentProject = MutableLiveData<Project>()

    init {
        // Insert a fake data set into database
        // https://github.com/blocoio/faker
        val faker = Faker()
        listOf(
                Project(id = 1, name = faker.name.name(), description = faker.lorem.paragraph()),
                Project(id = 2, name = faker.name.name(), description = faker.lorem.paragraph()),
                Project(id = 3, name = faker.name.name(), description = faker.lorem.paragraph()),
                Project(id = 4, name = faker.name.name(), description = faker.lorem.paragraph()),
                Project(id = 5, name = faker.name.name(), description = faker.lorem.paragraph()),
                Project(id = 6, name = faker.name.name(), description = faker.lorem.paragraph())
        ).forEach { insert(it) }
    }

    /* ==> Selected item */

    fun selectProject(project: Project) {
        currentProject.value = project
    }

    fun selectedProject(): LiveData<Project> = currentProject

    /* ==> db read operations */

    fun find(id: Int): LiveData<List<Project>> = dao.find(id)

    fun count(): Int = dao.count().toInt()

    /* ==> db write operations */

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
}
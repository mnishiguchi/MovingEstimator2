package com.mnishiguchi.movingestimator2.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mnishiguchi.movingestimator2.App
import com.mnishiguchi.movingestimator2.data.Pack
import com.mnishiguchi.movingestimator2.data.PackDao
import org.jetbrains.anko.doAsync

/**
 * A ViewModel for ProjectActivity. Must be initialized with a project id before use.
 * https://developer.android.com/topic/libraries/architecture/viewmodel.html
 */
class PackVM(val dao: PackDao = App.database.packDao()) : ViewModel() {

    /**
     * This DAO must be initialized with a project ID before use.
     */
    fun init(projectId: Int) {
        // Set the subject project ID.
        this.projectId = projectId

        doAsync {
            // Set the packs for that project ID.
            this@PackVM.packs = dao.all(projectId)
        }
    }

    /* ==> Data store */

    private var projectId: Int = 0
    private lateinit var packs: LiveData<List<Pack>>
    private val currentPack = MutableLiveData<Pack>()

    /* ==> packs */

    fun getPacks(): LiveData<List<Pack>> = packs

    /* ==> currentPack */

    fun selectPack(pack: Pack) {
        currentPack.value = pack
    }

    fun selectedPack(): LiveData<Pack> = currentPack

    /* ==> CRUD operations */

    fun isEmpty(): Boolean = packs.value?.isEmpty() ?: true

    fun create(): Pack = Pack().apply {
        val pack = this
        pack.projectId = this@PackVM.projectId
        doAsync { dao.insert(pack) }
    }

    fun insert(pack: Pack) {
        pack.projectId = this@PackVM.projectId
        doAsync { dao.insert(pack) }
    }
}

package com.mnishiguchi.movingestimator2.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

/**
 * Created by masa on 7/21/17.
 */
@Dao
interface ProjectDao {

    /* Queries */

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun all(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :arg0")
    fun find(id: Int): LiveData<List<Project>>

    @Query("SELECT COUNT(*) FROM projects")
    fun count(): Long

    /* Mutations */

    @Insert(onConflict = REPLACE)
    fun insert(project: Project): Long // Id

    @Delete
    fun delete(project: Project): Int // Number of deleted rows

    @Query("DELETE FROM projects WHERE id = :arg0")
    fun delete(id: Int): Int // Number of deleted rows
}

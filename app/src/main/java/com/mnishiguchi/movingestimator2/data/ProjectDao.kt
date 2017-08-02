package com.mnishiguchi.movingestimator2.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ProjectDao {

    /* Queries */

    @Query("SELECT * FROM projects ORDER BY id DESC")
    fun all(): LiveData<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :arg0")
    fun find(id: Int): LiveData<List<Project>>

    @Query("SELECT COUNT(*) FROM projects")
    fun countAll(): Long

    /* Mutations */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: Project): Long // Id

    @Delete
    fun delete(project: Project): Int // Number of deleted rows

    @Query("DELETE FROM projects WHERE id = :arg0")
    fun delete(id: Int): Int // Number of deleted rows
}

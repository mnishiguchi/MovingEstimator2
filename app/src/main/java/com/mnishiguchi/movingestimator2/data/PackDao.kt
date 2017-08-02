package com.mnishiguchi.movingestimator2.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface PackDao {

    /* Queries */

    @Query("SELECT * FROM packs WHERE project_id = :arg0 ORDER BY id DESC")
    fun all(projectId: Int): LiveData<List<Pack>>


    @Query("SELECT * FROM packs WHERE id = :arg0")
    fun find(id: Int): LiveData<List<Pack>>

    @Query("SELECT COUNT(*) FROM packs")
    fun countAll(): Long

    /* Mutations */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pack: Pack): Long // Id

    @Delete
    fun delete(pack: Pack): Int // Number of deleted rows

    @Query("DELETE FROM packs WHERE id = :arg0")
    fun delete(id: Int): Int // Number of deleted rows
}

package com.mnishiguchi.movingestimator2.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "projects")
data class Project(
        // https://developer.android.com/reference/android/arch/persistence/room/PrimaryKey.html#autoGenerate()
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0, // Insert methods treat 0 as not-set while inserting the item.

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "description")
        var description: String = "",

        @ColumnInfo(name = "move_date")
        var moveDate: Long = Date().time + Project.ONE_MONTH_IN_MILLIS
) {
    companion object {
        val ONE_MONTH_IN_MILLIS = 2.628e+9.toLong()
    }
}
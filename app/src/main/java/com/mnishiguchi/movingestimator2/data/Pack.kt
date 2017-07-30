package com.mnishiguchi.movingestimator2.data

import android.arch.persistence.room.*

// https://developer.android.com/reference/android/arch/persistence/room/ForeignKey.html
@Entity(
        tableName = "packs",
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Project::class,
                        parentColumns = arrayOf("id"),
                        childColumns = arrayOf("project_id"),
                        onDelete = ForeignKey.CASCADE
                )
        ),
        indices = arrayOf(
                Index(value = "project_id")
        )
)
data class Pack(
        // https://developer.android.com/reference/android/arch/persistence/room/PrimaryKey.html#autoGenerate()
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0, // Insert methods treat 0 as not-set while inserting the item.

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "volume")
        var volume: Float = 0.0F,

        @ColumnInfo(name = "quantity")
        var quantity: Int = 1,

        @ColumnInfo(name = "description")
        var description: String = "",

        @ColumnInfo(name = "project_id")
        var projectId: Int = 0
) {
    /**
     * A unique file name for this package.
     */
    fun photoFileName(): String = "IMG_$id.jpg"
}
package com.mnishiguchi.movingestimator2.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(
        entities = arrayOf(Project::class),
        version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        private const val DB_NAME = "app.db"

        fun createPersistentDatabase(context: Context): AppDatabase
                = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME).build()
    }
}
package com.mnishiguchi.movingestimator2

import android.app.Application
import com.mnishiguchi.movingestimator2.data.AppDatabase

/**
 * An application singleton that allows us to have an easier access to the application context.
 * Make sure that this class is registered in AndroidManifest.xml so that we can use it in the app.
 */
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
        val database: AppDatabase by lazy { AppDatabase.createPersistentDatabase(instance) }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
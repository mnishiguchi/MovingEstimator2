package com.mnishiguchi.movingestimator2

import android.app.Application
import com.mnishiguchi.movingestimator2.data.AppDatabase
import com.mnishiguchi.movingestimator2.util.dateFormat
import com.mnishiguchi.movingestimator2.util.longDateFormat
import com.mnishiguchi.movingestimator2.util.mediumDateFormat

/**
 * An application singleton that allows us to have an easier access to the application context.
 * Make sure that this class is registered in AndroidManifest.xml so that we can use it in the app.
 */
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
        val database: AppDatabase by lazy { AppDatabase.createPersistentDatabase(instance) }
        val dateFormat: java.text.DateFormat by lazy { instance.dateFormat() }
        val mediumDateFormat: java.text.DateFormat by lazy { instance.mediumDateFormat() }
        val longDateFormat: java.text.DateFormat by lazy { instance.longDateFormat() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
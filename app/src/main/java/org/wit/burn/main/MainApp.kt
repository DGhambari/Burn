package org.wit.burn.main

import android.app.Application
import org.wit.burn.models.BurnJSONStore
import org.wit.burn.models.BurnMemStore
import org.wit.burn.models.BurnStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var burns : BurnStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        burns = BurnJSONStore(applicationContext)
        i("Burn started")

    }
}

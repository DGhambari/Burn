package org.wit.burn.main

import android.app.Application
import org.wit.burn.models.BurnMemStore
import org.wit.burn.models.BurnModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    //val burns = ArrayList<BurnModel>()
    val burns = BurnMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Burn started")
//        burns.add(BurnModel("One", "About one..."))
//        burns.add(BurnModel("Two", "About two..."))
//        burns.add(BurnModel("Three", "About three..."))
    }
}
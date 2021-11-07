package com.mufiid.pilwali2020

import android.app.Application
import com.mufiid.pilwali2020.di.AppComponent
import com.mufiid.pilwali2020.di.DaggerAppComponent

class MyApplication: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
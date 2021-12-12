package com.mufiid.pilwali2020.di

import android.content.Context
import com.mufiid.pilwali2020.ui.login.LoginActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@Component(modules = [ServiceModule::class, RetrofitModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun inject(activity: LoginActivity)
}
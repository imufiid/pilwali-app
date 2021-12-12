package com.mufiid.pilwali2020.di

import com.mufiid.pilwali2020.data.remote.api.ApiService
import com.mufiid.pilwali2020.data.remote.api.CustomerService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {
    @Provides
    @Singleton
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    fun customerService(retrofit: Retrofit): CustomerService = retrofit.create(CustomerService::class.java)
}


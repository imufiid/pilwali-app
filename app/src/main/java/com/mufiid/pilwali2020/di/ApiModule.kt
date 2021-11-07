package com.mufiid.pilwali2020.di

import com.mufiid.pilwali2020.data.remote.api.ApiClient
import dagger.Module
import dagger.Provides

@Module
class ApiModule {
    @Provides
    fun provideApiService(apiClient: ApiClient) = apiClient.instance()
}
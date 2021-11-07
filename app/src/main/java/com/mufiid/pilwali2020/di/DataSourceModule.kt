package com.mufiid.pilwali2020.di

import com.mufiid.pilwali2020.data.remote.AuthRemoteDataSource
import com.mufiid.pilwali2020.data.remote.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class DataSourceModule {
    @Binds
    abstract fun provideAuthRemote(authRemoteSource: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}
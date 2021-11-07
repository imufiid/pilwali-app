package com.mufiid.pilwali2020.di

import com.mufiid.pilwali2020.data.repository.AuthRepository
import com.mufiid.pilwali2020.data.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideAuthRepo(authRepository: AuthRepositoryImpl): AuthRepository
}
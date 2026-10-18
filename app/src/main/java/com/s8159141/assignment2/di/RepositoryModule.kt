package com.s8159141.assignment2.di

import com.s8159141.assignment2.data.repository.AuthRepository
import com.s8159141.assignment2.data.repository.AuthRepositoryImpl
import com.s8159141.assignment2.data.repository.DashboardRepository
import com.s8159141.assignment2.data.repository.DashboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Binds repository interfaces to their implementations, so the rest of the app
 * depends only on the interfaces (which makes the ViewModels easy to unit-test).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}

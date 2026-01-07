package com.albertomrmekko.todolist.di

import com.albertomrmekko.todolist.domain.repository.ThemeRepository
import com.albertomrmekko.todolist.domain.repository.ThemeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemeModule {
    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        impl: ThemeRepositoryImpl
    ): ThemeRepository
}
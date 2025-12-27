package com.albertomrmekko.todolist.di

import android.content.Context
import androidx.room.Room
import com.albertomrmekko.todolist.data.local.AppDatabase
import com.albertomrmekko.todolist.data.local.dao.GroupDao
import com.albertomrmekko.todolist.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):
            AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "todolist_db").build()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideGroupDao(db: AppDatabase): GroupDao = db.groupDao()
}
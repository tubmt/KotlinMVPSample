package cminh.example.com.kotlinexample.data.source

import android.content.Context
import cminh.example.com.kotlinexample.data.source.local.TasksLocalDataSource
import cminh.example.com.kotlinexample.data.source.remote.TasksRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by cminh on 7/24/17.
 *
 */
@Module
class TasksRepositoryModule(val context: Context) {

    @Singleton
    @Provides
    @Local
    fun provideTasksLocalDataSource(): TasksDataSource = TasksLocalDataSource(context)

    @Singleton
    @Provides
    @Remote
    fun provideTasksRemoteDataSource(): TasksDataSource = TasksRemoteDataSource(context)
}
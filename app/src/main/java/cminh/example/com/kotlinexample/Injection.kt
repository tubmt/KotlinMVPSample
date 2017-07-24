package cminh.example.com.kotlinexample

import android.content.Context
import cminh.example.com.kotlinexample.addedittask.domain.usecase.GetTask
import cminh.example.com.kotlinexample.addedittask.domain.usecase.SaveTask
import cminh.example.com.kotlinexample.data.source.TasksRepository
import cminh.example.com.kotlinexample.data.source.local.TasksLocalDataSource
import cminh.example.com.kotlinexample.data.source.remote.TasksRemoteDataSource
import cminh.example.com.kotlinexample.tasks.domain.filter.FilterFactory
import cminh.example.com.kotlinexample.tasks.domain.usecase.ActivateTask
import cminh.example.com.kotlinexample.tasks.domain.usecase.ClearCompleteTasks
import cminh.example.com.kotlinexample.tasks.domain.usecase.CompleteTask
import cminh.example.com.kotlinexample.tasks.domain.usecase.GetTasks

/**
 * Created by cminh on 7/18/17.
 *
 */
class Injection {

    companion object {
        fun provideTasksRepository(context: Context): TasksRepository {
            return TasksRepository.getInstance(TasksRemoteDataSource.getInstance(context),
                    TasksLocalDataSource.getInstance(context))
        }

        fun provideGetTasks(context: Context): GetTasks {
            return GetTasks(provideTasksRepository(context), FilterFactory())
        }

        fun provideUseCaseHandler(): UseCaseHandler {
            return UseCaseHandler.getInstance()
        }

        fun provideGetTask(context: Context): GetTask {
            return GetTask(Injection.provideTasksRepository(context))
        }

        fun provideSaveTask(context: Context): SaveTask {
            return SaveTask(Injection.provideTasksRepository(context))
        }

        fun provideCompleteTasks(context: Context): CompleteTask {
            return CompleteTask(Injection.provideTasksRepository(context))
        }

        fun provideActivateTask(context: Context): ActivateTask {
            return ActivateTask(Injection.provideTasksRepository(context))
        }

        fun provideClearCompleteTasks(context: Context): ClearCompleteTasks {
            return ClearCompleteTasks(Injection.provideTasksRepository(context))
        }
    }
}
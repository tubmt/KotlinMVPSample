package cminh.example.com.kotlinexample

import cminh.example.com.kotlinexample.data.source.TasksRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Created by cminh on 7/24/17.
 *
 */
@Singleton
@Component(modules = arrayOf(KotlinModule::class))
interface KotlinComponent {
    fun getTasksRepository() : TasksRepository
}
package cminh.example.com.kotlinexample.data.source.remote

import android.content.Context
import cminh.example.com.kotlinexample.data.source.TasksDataSource
import cminh.example.com.kotlinexample.data.source.local.TasksDbHelper
import cminh.example.com.kotlinexample.tasks.domain.model.Task

/**
 * Created by cminh on 7/13/17.
 * 
 */
class TasksRemoteDataSource constructor(context: Context, var mDbHelper: TasksDbHelper = TasksDbHelper(context)): TasksDataSource {

    companion object {
        private var INSTANCE: TasksRemoteDataSource? = null

        fun getInstance(context: Context): TasksRemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = TasksRemoteDataSource(context)
            }
            return INSTANCE!!
        }
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        
    }

    override fun addOrUpdateTask(task: Task) {
        
    }

    override fun completeTask(task: Task) {
        
    }

    override fun completeTask(taskId: String) {
        
    }

    override fun activateTask(task: Task) {
        
    }

    override fun activateTask(taskId: String) {
        
    }

    override fun clearCompletedTasks() {
        
    }

    override fun refreshTasks() {
        
    }

    override fun deleteAllTasks() {
        
    }

    override fun deleteTask(taskId: String) {
        
    }
}
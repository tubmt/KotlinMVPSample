package cminh.example.com.kotlinexample.data.source

import cminh.example.com.kotlinexample.tasks.domain.model.Task
import java.util.*

/**
 * Created by cminh on 7/10/17.
 *
 */
class TasksRepository constructor(var mTasksLocalDataSource: TasksDataSource,
                                  var mTasksRemoteDataSource: TasksDataSource,
                                  var mCacheIsDirty: Boolean = false): TasksDataSource {
    private var mCachedTasks: HashMap<String, Task>? = null

    companion object {
        private var INSTANCE: TasksRepository? = null

        fun getInstance(tasksLocalDataSource: TasksDataSource, tasksRemoteDataSource: TasksDataSource): TasksRepository {
            if (INSTANCE == null) {
                INSTANCE = TasksRepository(tasksLocalDataSource, tasksRemoteDataSource)
            }
            return INSTANCE!!
        }
    }

    fun destroyInstance() { INSTANCE = null }

    private fun refreshCache(tasks: List<Task>) {
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap<String, Task>()
        }
        mCachedTasks?.clear()
        for (task in tasks) {
            mCachedTasks?.put(task.mId, task)
        }
        mCacheIsDirty = false
    }

    private fun refreshLocalDataSource(tasks: List<Task>) {
        mTasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            mTasksLocalDataSource.addOrUpdateTask(task)
        }
    }

    private fun getTaskWithId(id: String = ""): Task? {
        if (mCachedTasks == null || mCachedTasks!!.isEmpty()) {
            return null
        } else {
            return mCachedTasks?.get(id)
        }
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        mTasksLocalDataSource.getTasks(callback)
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        mTasksLocalDataSource.getTask(taskId, callback)
    }

    override fun addOrUpdateTask(task: Task) {
        mTasksLocalDataSource.addOrUpdateTask(task)
        mTasksRemoteDataSource.addOrUpdateTask(task)

        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap<String, Task>()
        }
        mCachedTasks?.put(task.mId, task)
    }

    override fun completeTask(task: Task) {
        
    }

    override fun completeTask(taskId: String) {
        mTasksLocalDataSource.completeTask(taskId)
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
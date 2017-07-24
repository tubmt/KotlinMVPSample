package cminh.example.com.kotlinexample.data.source.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import cminh.example.com.kotlinexample.data.source.TasksDataSource
import cminh.example.com.kotlinexample.tasks.domain.model.Task
import java.util.*

/**
 * Created by cminh on 7/10/17.
 * 
 */
class TasksLocalDataSource constructor(context: Context, var mDbHelper: TasksDbHelper = TasksDbHelper(context)): TasksDataSource {

    companion object {
        var INSTANCE: TasksLocalDataSource? = null

        fun getInstance(context: Context): TasksLocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = TasksLocalDataSource(context)
            }
            return INSTANCE!!
        }
    }

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        val tasks: ArrayList<Task> = ArrayList<Task>()
        val db = mDbHelper.readableDatabase
        val projection = arrayOf(
                Task.COLUMN_ID,
                Task.COLUMN_NAME_ENTRY_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DESCRIPTION,
                Task.COLUMN_NAME_COMPLETED)

        val c: Cursor? = db.query(Task.TABLE_NAME, projection, null, null, null, null, null)

        if (c != null && c.count > 0) {
            while (c.moveToNext()) {
                val itemId = c.getString(c.getColumnIndexOrThrow(Task.COLUMN_NAME_ENTRY_ID))
                val title = c.getString(c.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val description = c.getString(c.getColumnIndexOrThrow(Task.COLUMN_NAME_DESCRIPTION))
                val completed = c.getInt(c.getColumnIndexOrThrow(Task.COLUMN_NAME_COMPLETED)) == 1
                tasks.add(Task(title, description, itemId, completed))
            }
        }

        c?.close()
        db.close()

        if (tasks.isEmpty()) {
            callback.onDataNotAvailable()
        } else {
            callback.onTasksLoaded(tasks)
        }
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val db = mDbHelper.readableDatabase
        val projection = arrayOf(
                Task.COLUMN_NAME_ENTRY_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DESCRIPTION,
                Task.COLUMN_NAME_COMPLETED)
        val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?"
        val c: Cursor? = db.query(Task.TABLE_NAME, projection, selection, arrayOf(taskId), null, null, null)

        var task: Task? = null
        if (c != null && c.count > 0) {
            c.moveToFirst()
            val itemId = c.getString(c.getColumnIndexOrThrow(Task.COLUMN_NAME_ENTRY_ID))
            val title = c.getString(c.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val description = c.getString(c.getColumnIndexOrThrow(Task.COLUMN_NAME_DESCRIPTION))
            val completed = c.getInt(c.getColumnIndexOrThrow(Task.COLUMN_NAME_COMPLETED)) == 1
            task = Task(title, description, itemId, completed)
        }

        c?.close()
        db.close()

        if (task == null) {
            callback.onDataNotAvailable()
        } else {
            callback.onTaskLoaded(task)
        }
    }

    override fun addOrUpdateTask(task: Task) {

        getTask(task.mId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(dbTask: Task) {
                val db = mDbHelper.writableDatabase
                val values = ContentValues()
                values.put(Task.COLUMN_NAME_TITLE, task.mTitle)
                values.put(Task.COLUMN_NAME_DESCRIPTION, task.mDescription)
                values.put(Task.COLUMN_NAME_COMPLETED, task.mCompleted)

                val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?"
                db.update(Task.TABLE_NAME, values, selection, arrayOf(dbTask.mId))
                db.close()
            }

            override fun onDataNotAvailable() {
                val db = mDbHelper.writableDatabase
                val values = ContentValues()
                values.put(Task.COLUMN_NAME_ENTRY_ID, task.mId)
                values.put(Task.COLUMN_NAME_TITLE, task.mTitle)
                values.put(Task.COLUMN_NAME_DESCRIPTION, task.mDescription)
                values.put(Task.COLUMN_NAME_COMPLETED, task.mCompleted)

                db.insert(Task.TABLE_NAME, null, values)
                db.close()
            }
        })
    }

    override fun completeTask(task: Task) {
        val db = mDbHelper.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_COMPLETED, true)

        val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        db.update(Task.TABLE_NAME, values, selection, arrayOf(task.mId))
        db.close()
    }

    override fun completeTask(taskId: String) {
        val db = mDbHelper.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_COMPLETED, true)

        val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        db.update(Task.TABLE_NAME, values, selection, arrayOf(taskId))
        db.close()
    }

    override fun activateTask(task: Task) {
        val db = mDbHelper.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_COMPLETED, false)

        val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        db.update(Task.TABLE_NAME, values, selection, arrayOf(task.mId))
        db.close()
    }

    override fun activateTask(taskId: String) {
        val db = mDbHelper.writableDatabase

        val values = ContentValues()
        values.put(Task.COLUMN_NAME_COMPLETED, false)

        val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        db.update(Task.TABLE_NAME, values, selection, arrayOf(taskId))
        db.close()
    }

    override fun clearCompletedTasks() {
        val db = mDbHelper.writableDatabase

        val selection = Task.COLUMN_NAME_COMPLETED + " LIKE ?";

        db.delete(Task.TABLE_NAME, selection, arrayOf("1"))
        db.close()
    }

    override fun refreshTasks() {
    }

    override fun deleteAllTasks() {
        val db = mDbHelper.writableDatabase

        db.delete(Task.TABLE_NAME, null, null)
        db.close()
    }

    override fun deleteTask(taskId: String) {
        val db = mDbHelper.writableDatabase
        val selection = Task.COLUMN_NAME_ENTRY_ID + " LIKE ?"

        db.delete(Task.TABLE_NAME, selection, arrayOf(taskId))
        db.close()
    }
}
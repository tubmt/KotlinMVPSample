package cminh.example.com.kotlinexample.tasks.domain.model

import android.text.TextUtils
import java.util.*

/**
 * Created by cminh on 7/10/17.
 *
 */
class Task(var mTitle: String, var mDescription: String, var mId: String = UUID.randomUUID().toString(), var mCompleted: Boolean = false) {

    fun isEmpty(): Boolean = TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mDescription)

    fun isActive(): Boolean = !mCompleted

    override fun equals(other: Any?): Boolean {
        if (this == other) return true
        if (other == null || other is Task) return false
        val task: Task = other as Task

        return Objects.equals(mId, task.mId) && Objects.equals(mTitle, task.mTitle) && Objects.equals(mDescription, task.mDescription)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(this)
    }

    companion object {
        val TABLE_NAME = "task"
        val COLUMN_ID = "id"
        val COLUMN_NAME_ENTRY_ID = "entryid"
        val COLUMN_NAME_TITLE = "title"
        val COLUMN_NAME_DESCRIPTION = "description"
        val COLUMN_NAME_COMPLETED = "completed"
    }
}
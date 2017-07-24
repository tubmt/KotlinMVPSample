package cminh.example.com.kotlinexample.tasks

import cminh.example.com.kotlinexample.BasePresenter
import cminh.example.com.kotlinexample.BaseView
import cminh.example.com.kotlinexample.tasks.domain.model.Task
import cminh.example.com.kotlinexample.tasks.domain.model.TasksFilterType

/**
 * Created by cminh on 7/10/17.
 *
 */
interface TasksContract {

    interface View: BaseView {
        fun setLoadingIndicator(active: Boolean)

        fun showTasks(tasks: List<Task>)

        fun showAddTask()

        fun showTaskDetailsUi(taskId: String)

        fun showTaskMarkedComplete()

        fun showTaskMarkedActive()

        fun showCompletedTasksCleared()

        fun showLoadingTasksError()

        fun showNoTasks()

        fun showActiveFilterLabel()

        fun showCompletedFilterLabel()

        fun showAllFilterLabel()

        fun showNoActiveTasks()

        fun showNoCompletedTasks()

        fun showSuccessfullySavedMessage()

        fun isActive(): Boolean

        fun showFilteringPopUpMenu()
    }

    interface Presenter: BasePresenter {
        override fun start() {}

        override fun stop() {}

        fun result(requestCode: Int, resultCode: Int)

        fun loadTasks(forceUpdate: Boolean)

        fun addNewTask()

        fun openTaskDetails(requestedTask: Task)

        fun completeTask(completedTask: Task)

        fun activateTask(activeTask: Task)

        fun clearCompletedTasks()

        fun setFiltering(requestType: TasksFilterType)

        fun getFiltering(): TasksFilterType
    }
}
package cminh.example.com.kotlinexample.tasks

import android.os.Looper
import android.util.Log
import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.UseCaseHandler
import cminh.example.com.kotlinexample.tasks.domain.model.Task
import cminh.example.com.kotlinexample.tasks.domain.model.TasksFilterType
import cminh.example.com.kotlinexample.tasks.domain.usecase.ActivateTask
import cminh.example.com.kotlinexample.tasks.domain.usecase.ClearCompleteTasks
import cminh.example.com.kotlinexample.tasks.domain.usecase.CompleteTask
import cminh.example.com.kotlinexample.tasks.domain.usecase.GetTasks

/**
 * Created by cminh on 7/13/17.
 * 
 */
class TasksPresenter(val mTasksView: TasksContract.View, val mGetTasks: GetTasks,
                     val mCompleteTask: CompleteTask, val mActiveTask: ActivateTask,
                     val mClearCompleteTasks: ClearCompleteTasks,
                     val mUseCaseHandler: UseCaseHandler,
                     var mCurrentFiltering: TasksFilterType = TasksFilterType.ALL_TASKS,
                     var mFirstLoad: Boolean = true): TasksContract.Presenter  {

    companion object {
        private val TAG = "TasksPresenter"
    }

    init {
        mTasksView.setPresenter(this)
    }

    override fun start() {
        loadTasks(false)
    }

    override fun stop() {
        // TODO
    }

    override fun result(requestCode: Int, resultCode: Int) {
        // TODO
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || mFirstLoad, true)
        // TODO
    }

    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true)
        }

        val requestValues: GetTasks.Companion.RequestValues  = GetTasks.Companion.RequestValues(mCurrentFiltering, forceUpdate)
        mUseCaseHandler.execute(mGetTasks, requestValues,
                object : UseCase.UseCaseCallback<GetTasks.Companion.ResponseValue> {
                    override fun onSuccess(response: GetTasks.Companion.ResponseValue) {
                        val tasks: List<Task> = response.mTasks

                        if (!mTasksView.isActive()) {
                            return
                        }

                        if (showLoadingUI) {
                            mTasksView.setLoadingIndicator(false)
                        }

                        android.os.Handler(Looper.getMainLooper()).post {
                            processTasks(tasks)
                        }
                    }

                    override fun onError() {
                        if (!mTasksView.isActive()) {
                            return
                        }

                        mTasksView.showLoadingTasksError()
                    }
                })

    }


    private fun processTasks(tasks: List<Task>) {
        Log.e(TAG, "processTasks")
        if (tasks.isEmpty()) {
            Log.e(TAG, "processTasks EMTPY")
            processEmptyTasks()
        } else {
            mTasksView.showTasks(tasks)

            showFilterLabel()
        }
    }

    private fun showFilterLabel() {
        Log.d(TAG, "showFilterLabel")
        when (mCurrentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> mTasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> mTasksView.showCompletedFilterLabel()
            else -> mTasksView.showAllFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when (mCurrentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> mTasksView.showNoActiveTasks()
            TasksFilterType.COMPLETED_TASKS -> mTasksView.showNoCompletedTasks()
            else -> mTasksView.showNoTasks()
        }
    }

    override fun addNewTask() {
        mTasksView.showAddTask()
    }

    override fun openTaskDetails(requestedTask: Task) {
        mTasksView.showTaskDetailsUi(requestedTask.mId)
    }

    override fun completeTask(completedTask: Task) {
        mUseCaseHandler.execute(mCompleteTask, CompleteTask.Companion.RequestValues(completedTask.mId),
                object : UseCase.UseCaseCallback<CompleteTask.Companion.ResponseValue> {
                    override fun onSuccess(response: CompleteTask.Companion.ResponseValue) {
                        mTasksView.showTaskMarkedComplete()
                        loadTasks(false, false)
                    }

                    override fun onError() {
                        mTasksView.showLoadingTasksError()
                    }
                })
    }

    override fun activateTask(activeTask: Task) {
        mUseCaseHandler.execute(mActiveTask, ActivateTask.Companion.RequestValues(activeTask.mId),
                object : UseCase.UseCaseCallback<ActivateTask.Companion.ResponseValue> {
                    override fun onSuccess(response: ActivateTask.Companion.ResponseValue) {
                        mTasksView.showTaskMarkedActive()
                        loadTasks(false, false)
                    }

                    override fun onError() {
                        mTasksView.showLoadingTasksError()
                    }
                })
    }

    override fun clearCompletedTasks() {
        mUseCaseHandler.execute(mClearCompleteTasks, ClearCompleteTasks.Companion.RequestValues(),
                object : UseCase.UseCaseCallback<ClearCompleteTasks.Companion.ResponseValue> {
                    override fun onSuccess(response: ClearCompleteTasks.Companion.ResponseValue) {
                        mTasksView.showCompletedTasksCleared()
                        loadTasks(false, false)
                    }

                    override fun onError() {
                        mTasksView.showLoadingTasksError()
                    }
                })
    }

    override fun setFiltering(requestType: TasksFilterType) {
        mCurrentFiltering = requestType
    }

    override fun getFiltering() = mCurrentFiltering
}
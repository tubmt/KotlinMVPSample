package cminh.example.com.kotlinexample.addedittask

import android.text.TextUtils
import android.util.Log
import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.UseCaseHandler
import cminh.example.com.kotlinexample.addedittask.domain.usecase.GetTask
import cminh.example.com.kotlinexample.addedittask.domain.usecase.SaveTask
import cminh.example.com.kotlinexample.tasks.domain.model.Task

class AddEditTaskPresenter(val taskId: String = "", val getTask: GetTask, val saveTask: SaveTask,
                           val mUseCaseHandler: UseCaseHandler, val mView: AddEditTaskContract.View) : AddEditTaskContract.Presenter {

    companion object {
        private val TAG = "AddEditTaskPresenter"
    }

    init {
        mView.setPresenter(this)
    }

    override fun start() {
        loadTask(taskId)
    }

    override fun stop() {
        // TODO
    }

    override fun loadTask(taskId: String) {
        if (TextUtils.isEmpty(taskId)) {
            mView.setTitle("")
            mView.setDescription("")
            return
        }

        mUseCaseHandler.execute(getTask, GetTask.Companion.RequestValues(taskId), object : UseCase.UseCaseCallback<GetTask.Companion.ResponseValue> {
            override fun onSuccess(response: GetTask.Companion.ResponseValue) {
                val task = response.task
                mView.setTitle(task.mTitle)
                mView.setDescription(task.mDescription)
            }

            override fun onError() {
                Log.e(TAG, "loadTask - onError!!!")
            }
        })
    }

    override fun saveTask(title: String, description: String) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            mView.showInvalidData()
            return
        }

        val task: Task = Task(title, description)

        if (!TextUtils.isEmpty(taskId))
            task.mId = taskId

        mUseCaseHandler.execute(saveTask, SaveTask.Companion.RequestValues(task), object : UseCase.UseCaseCallback<SaveTask.Companion.ResponseValue> {
            override fun onSuccess(response: SaveTask.Companion.ResponseValue) {
                mView.showTask()
            }

            override fun onError() {
                mView.showError()
            }
        })
    }
}
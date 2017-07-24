package cminh.example.com.kotlinexample.addedittask.domain.usecase

import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.data.source.TasksDataSource
import cminh.example.com.kotlinexample.data.source.TasksRepository
import cminh.example.com.kotlinexample.tasks.domain.model.Task

/**
 * Created by cminh on 7/17/17.
 *
 */
class GetTask(val tasksRepository: TasksRepository): UseCase<GetTask.Companion.RequestValues, GetTask.Companion.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        val taskId = requestValues.taskId

        tasksRepository.getTask(taskId, object : TasksDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {
                mUseCaseCallback?.onSuccess(GetTask.Companion.ResponseValue(task))
            }

            override fun onDataNotAvailable() {
                mUseCaseCallback?.onError()
            }
        })
    }

    companion object {
        class RequestValues(val taskId: String): UseCase.RequestValues
        class ResponseValue(val task: Task): UseCase.ResponseValue
    }
}
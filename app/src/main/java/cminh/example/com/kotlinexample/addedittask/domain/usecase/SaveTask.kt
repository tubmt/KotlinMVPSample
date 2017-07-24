package cminh.example.com.kotlinexample.addedittask.domain.usecase

import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.data.source.TasksRepository
import cminh.example.com.kotlinexample.tasks.domain.model.Task

/**
 * Created by cminh on 7/17/17.
 *
 */
class SaveTask(val tasksRepository: TasksRepository): UseCase<SaveTask.Companion.RequestValues, SaveTask.Companion.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        val task: Task = requestValues.task

        tasksRepository.addOrUpdateTask(task)
        mUseCaseCallback?.onSuccess(SaveTask.Companion.ResponseValue())
    }

    companion object {
        class RequestValues(val task: Task): UseCase.RequestValues
        class ResponseValue: UseCase.ResponseValue
    }
}
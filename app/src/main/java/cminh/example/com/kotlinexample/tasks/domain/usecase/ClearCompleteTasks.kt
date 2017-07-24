package cminh.example.com.kotlinexample.tasks.domain.usecase

import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.data.source.TasksRepository

/**
 * Created by cminh on 7/13/17.
 *
 */
class ClearCompleteTasks(val mTasksRepository: TasksRepository):
        UseCase<ClearCompleteTasks.Companion.RequestValues, ClearCompleteTasks.Companion.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        mTasksRepository.clearCompletedTasks()
        mUseCaseCallback?.onSuccess(ResponseValue())
    }

    companion object {
        class RequestValues: UseCase.RequestValues
        class ResponseValue: UseCase.ResponseValue
    }
}
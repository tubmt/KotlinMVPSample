package cminh.example.com.kotlinexample.tasks.domain.usecase

import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.data.source.TasksRepository

/**
 * Created by cminh on 7/13/17.
 *
 */
class CompleteTask(val mTasksRepository: TasksRepository):
        UseCase<CompleteTask.Companion.RequestValues, CompleteTask.Companion.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        mTasksRepository.completeTask(requestValues.mCompletedTask)
        mUseCaseCallback?.onSuccess(ResponseValue())
    }

    companion object {
        class RequestValues(val mCompletedTask: String): UseCase.RequestValues
        class ResponseValue: UseCase.ResponseValue
    }
}
package cminh.example.com.kotlinexample.tasks.domain.usecase

import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.data.source.TasksRepository

/**
 * Created by cminh on 7/10/17.
 *
 */
class ActivateTask(val mTasksRepository: TasksRepository) :
        UseCase<ActivateTask.Companion.RequestValues, ActivateTask.Companion.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        mTasksRepository.activateTask(requestValues.mActivityTask)
        mUseCaseCallback?.onSuccess(ResponseValue())
    }

    companion object {
        class RequestValues(val mActivityTask: String): UseCase.RequestValues

        class ResponseValue: UseCase.ResponseValue
    }
}
package cminh.example.com.kotlinexample.tasks.domain.usecase

import cminh.example.com.kotlinexample.UseCase
import cminh.example.com.kotlinexample.data.source.TasksDataSource
import cminh.example.com.kotlinexample.data.source.TasksRepository
import cminh.example.com.kotlinexample.tasks.domain.filter.FilterFactory
import cminh.example.com.kotlinexample.tasks.domain.model.Task
import cminh.example.com.kotlinexample.tasks.domain.model.TasksFilterType

/**
 * Created by cminh on 7/13/17.
 *
 */
class GetTasks(val mTasksRepository: TasksRepository, val mFilterFactory: FilterFactory):
        UseCase<GetTasks.Companion.RequestValues, GetTasks.Companion.ResponseValue>() {

    override fun executeUseCase(requestValues: RequestValues) {
        if (requestValues.mForceUpdate) {
            mTasksRepository.refreshTasks()
        }

        mTasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                val filterType = requestValues.mFilterType
                val taskFilter = mFilterFactory.create(filterType)

                var tasksFiltered: List<Task>? = taskFilter?.filter(tasks)
                if (tasksFiltered == null) {
                    tasksFiltered = ArrayList<Task>()
                }
                mUseCaseCallback?.onSuccess(ResponseValue(tasksFiltered))
            }

            override fun onDataNotAvailable() {
                mUseCaseCallback?.onError()
            }
        })
    }

    companion object {
        class RequestValues(val mFilterType: TasksFilterType, val mForceUpdate: Boolean) : UseCase.RequestValues
        class ResponseValue(val mTasks: List<Task>): UseCase.ResponseValue
    }
}
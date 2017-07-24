package cminh.example.com.kotlinexample.tasks.domain.filter

import cminh.example.com.kotlinexample.tasks.domain.model.TasksFilterType
import java.util.*

/**
 * Created by cminh on 7/10/17.
 *
 */
class FilterFactory {

    init {
        mFilters.put(TasksFilterType.ALL_TASKS, FilterAllTaskFilter())
        mFilters.put(TasksFilterType.COMPLETED_TASKS, CompleteTaskFilter())
        mFilters.put(TasksFilterType.ACTIVE_TASKS, ActiveTaskFilter())
    }

    fun create(filterType: TasksFilterType): TaskFilter? {
        return mFilters[filterType]
    }

    companion object {
        val mFilters = HashMap<TasksFilterType, TaskFilter>()
    }
}
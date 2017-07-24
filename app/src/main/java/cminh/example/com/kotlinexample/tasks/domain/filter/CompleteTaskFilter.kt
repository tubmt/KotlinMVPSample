package cminh.example.com.kotlinexample.tasks.domain.filter

import cminh.example.com.kotlinexample.tasks.domain.model.Task
import java.util.*

/**
 * Created by cminh on 7/10/17.
 *
 */
class CompleteTaskFilter: TaskFilter {
    override fun filter(tasks: List<Task>): List<Task> {
        val resultTasks = ArrayList<Task>()

        for (task in tasks) {
            if (task.mCompleted) {
                resultTasks.add(task)
            }
        }
        return resultTasks
    }
}
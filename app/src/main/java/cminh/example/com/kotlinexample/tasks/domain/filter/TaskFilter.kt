package cminh.example.com.kotlinexample.tasks.domain.filter

import cminh.example.com.kotlinexample.tasks.domain.model.Task

/**
 * Created by cminh on 7/10/17.
 *
 */
interface TaskFilter {
    fun filter(tasks: List<Task>): List<Task>
}
package cminh.example.com.kotlinexample.addedittask

import cminh.example.com.kotlinexample.BasePresenter
import cminh.example.com.kotlinexample.BaseView

/**
 * Created by cminh on 7/17/17.
 *
 */
interface AddEditTaskContract {
    interface View : BaseView {
        override fun <P : BasePresenter> setPresenter(presenter: P) {
        }

        fun showError()

        fun showTask()

        fun setTitle(title: String)

        fun setDescription(description: String)

        fun showInvalidData()
    }

    interface Presenter : BasePresenter {
        fun loadTask(taskId: String)

        fun saveTask(title: String, description: String)
    }
}
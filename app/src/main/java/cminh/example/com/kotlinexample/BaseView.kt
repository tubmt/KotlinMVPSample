package cminh.example.com.kotlinexample

/**
 * Created by cminh on 7/8/17.
 *
 */
interface BaseView {
    fun <P: BasePresenter> setPresenter(presenter: P)
}
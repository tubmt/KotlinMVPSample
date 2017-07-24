package cminh.example.com.kotlinexample

/**
 * Created by cminh on 7/8/17.
 *
 */
interface UseCaseScheduler {

    fun execute(runnable: Runnable) { }

    fun <V: UseCase.ResponseValue> notifyResponse(response: V, useCaseCallback: UseCase.UseCaseCallback<V>)

    fun <V: UseCase.ResponseValue> onError(useCaseCallback: UseCase.UseCaseCallback<V>)
}
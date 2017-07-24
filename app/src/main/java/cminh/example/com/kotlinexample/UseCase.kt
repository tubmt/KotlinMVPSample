package cminh.example.com.kotlinexample

/**
 * Created by cminh on 7/8/17.
 *
 */
abstract class UseCase<Q: UseCase.RequestValues, P: UseCase.ResponseValue> (var mRequestValues: Q? = null, var mUseCaseCallback: UseCaseCallback<P>? = null) {

    fun run() {
        executeUseCase(mRequestValues!!)
    }

    protected abstract fun executeUseCase(requestValues :Q)

    interface RequestValues

    interface ResponseValue

    interface UseCaseCallback<R> {
        fun onSuccess(response: R): Unit
        fun onError(): Unit
    }
}
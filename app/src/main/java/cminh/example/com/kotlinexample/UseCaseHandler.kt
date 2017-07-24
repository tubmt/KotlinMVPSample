package cminh.example.com.kotlinexample

/**
 * Created by cminh on 7/8/17.
 *
 */
class UseCaseHandler(private val mUseCaseScheduler: UseCaseScheduler) {

    fun <T: UseCase.RequestValues, R: UseCase.ResponseValue> execute(useCase: UseCase<T, R>, values: T, callback: UseCase.UseCaseCallback<R>) {
        useCase.mRequestValues = values;
        useCase.mUseCaseCallback = callback;

        mUseCaseScheduler.execute(Runnable { useCase.run() })
    }

    fun <V: UseCase.ResponseValue> notifyResponse(response: V, useCaseCallback: UseCase.UseCaseCallback<V>) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback)
    }

    private fun <V: UseCase.ResponseValue> notifyError(useCaseCallback: UseCase.UseCaseCallback<V>) {
        mUseCaseScheduler.onError(useCaseCallback)
    }

    class UiCallbackWrapper<V: UseCase.ResponseValue>(
            private val mCallback: UseCase.UseCaseCallback<V>,
            private val mUseCaseHandler: UseCaseHandler) : UseCase.UseCaseCallback<V> {

        override fun onSuccess(response: V) {
            this.mUseCaseHandler.notifyResponse(response, mCallback)
        }

        override fun onError() {
            this.mUseCaseHandler.notifyError(mCallback)
        }
    }

    companion object {
        private var INSTANCE: UseCaseHandler? = null

        fun getInstance(): UseCaseHandler {
            if (INSTANCE == null) {
                INSTANCE = UseCaseHandler(UseCaseThreadPoolScheduler())
            }
            return INSTANCE!!
        }
    }
}
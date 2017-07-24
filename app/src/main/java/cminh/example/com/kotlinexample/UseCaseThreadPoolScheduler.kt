package cminh.example.com.kotlinexample

import android.os.Handler
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by cminh on 7/8/17.
 *
 */
class UseCaseThreadPoolScheduler: UseCaseScheduler {

    private val mHandler: Handler = Handler()
    private val mThreadPoolExecutor: ThreadPoolExecutor

    init {
        mThreadPoolExecutor = ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT.toLong(), TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(POOL_SIZE))
    }

    override fun execute(runnable: Runnable) {
        mThreadPoolExecutor.execute(runnable)
    }

    override fun <V : UseCase.ResponseValue> notifyResponse(response: V, useCaseCallback: UseCase.UseCaseCallback<V>) {
        mHandler.post( { useCaseCallback.onSuccess(response) })
    }

    override fun <V : UseCase.ResponseValue> onError(useCaseCallback: UseCase.UseCaseCallback<V>) {
        mHandler.post( { useCaseCallback.onError() })
    }

    companion object {
        private val POOL_SIZE: Int = 2
        private val MAX_POOL_SIZE: Int = 4
        private val TIMEOUT: Int = 30;
    }
}
package com.technoidentity.vitalz.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

suspend fun <T : Any> runIO(
    dispatcherProvider: CoroutinesDispatcherProvider,
    work: suspend () -> T
): Flow<ResultHandler<T>> =
    flow {
        withContext(dispatcherProvider.io) {
            try {
                val result = work()
                withContext(dispatcherProvider.main) {
                    when (result) {
                        is Throwable -> ResultHandler.Error(result)
                        else -> ResultHandler.Success(result)
                    }
                }
            } catch (ex: Exception) {
                withContext(dispatcherProvider.main) {
                    ResultHandler.Error(ex)
                }
            }
        }
    }

sealed class ResultHandler<out T : Any>(val data: T?, val message: Any?) {
    class Success<out T : Any>(data: T) : ResultHandler<T>(data, null)
    class Error<T : Any>(message: Any) : ResultHandler<T>(null, message)
}
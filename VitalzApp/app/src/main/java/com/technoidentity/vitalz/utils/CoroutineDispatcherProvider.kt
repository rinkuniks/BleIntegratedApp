package com.technoidentity.vitalz.utils

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutinesDispatcherProvider{
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}
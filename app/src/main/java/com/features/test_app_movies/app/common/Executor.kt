package com.features.test_app_movies.app.common

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object Executor {
    fun IOThread(t: Runnable?) {
        val IO_EXECUTOR: ExecutorService = Executors.newSingleThreadExecutor()
        IO_EXECUTOR.execute(t)
    }
}
package net.asere.omni.result

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first

open class ResultIntent<Result>(
    val job: Job,
    protected val scope: ResultScope<Result>
) {
    suspend fun awaitResult() = scope.result.first()
}
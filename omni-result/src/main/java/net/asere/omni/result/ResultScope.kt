package net.asere.omni.result

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import net.asere.omni.core.ExecutionScope
import net.asere.omni.core.OmniHostDsl

class ResultScope<Result> : ExecutionScope() {

    private val mutableResult: MutableSharedFlow<Result> = MutableSharedFlow()
    internal val result: SharedFlow<Result> = mutableResult
    internal var errorBlock: (suspend (Throwable) -> Result)? = null

    internal suspend fun setResult(result: Result) {
        mutableResult.emit(result)
    }
}

@OmniHostDsl
fun <Result> ResultScope<Result>.onError(
    block: suspend (Throwable) -> Result
) {
    errorBlock = block
}
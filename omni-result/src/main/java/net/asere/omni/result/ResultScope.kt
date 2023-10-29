package net.asere.omni.result

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import net.asere.omni.core.ExecutionScope
import net.asere.omni.core.OmniHostDsl

open class ResultScope<Result> : ExecutionScope() {

    private val mutableResult = Channel<Result>(capacity = Channel.UNLIMITED)
    internal val result: Flow<Result> = mutableResult.receiveAsFlow()
    internal var errorBlock: (suspend (Throwable) -> Result)? = null

    internal suspend fun setResult(result: Result) {
        mutableResult.send(result)
    }
}

@OmniHostDsl
fun <Result> ResultScope<Result>.onError(
    block: suspend (Throwable) -> Result
) {
    errorBlock = block
}
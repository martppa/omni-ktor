package net.asere.omni.result

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import net.asere.omni.core.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface ResultContainerHost : ContainerHost {
    override val container: ResultContainer
}

@OmniHostDsl
fun <Result> ResultContainerHost.resultIntent(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend ResultScope<Result>.() -> Result
): ResultIntent<Result> {
    val scope = ResultScope<Result>()
    (scope as ExecutionScope).onError {
        CoroutineScope(context = context).launch {
            if (scope.errorBlock != null) {
                scope.setResult(scope.errorBlock!!.invoke(it))
            } else {
                throw RuntimeException("Exception mapper not set!")
            }
        }
    }
    val job = execute(
        context = context,
        start = start,
        scope = scope,
        block = {
            scope.setResult(block())
        }
    )

    return ResultIntent(
        job = job,
        scope = scope,
    )
}
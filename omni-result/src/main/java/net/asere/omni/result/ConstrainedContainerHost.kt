package net.asere.omni.result

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import net.asere.omni.core.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface ConstrainedContainerHost<Result> : ResultContainerHost {
    override val container: ConstrainedResultContainer<Result>
}

@OmniHostDsl
@Suppress("UNCHECKED_CAST")
fun <Result> ConstrainedContainerHost<Result>.constrainedIntent(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend ResultScope<Result>.() -> Result
): ResultIntent<Result> {
    val scope = ResultScope<Result>()

    (scope as ExecutionScope).onError {
        CoroutineScope(context = context).launch {
            if (scope.errorBlock != null) {
                scope.setResult(scope.errorBlock!!.invoke(it))
            } else if (container.exceptionMapper != null) {
                scope.setResult(container.exceptionMapper!!.valueOf(it))
            } else {
                throw RuntimeException("Result error mapper not set! Make sure you set " +
                        "the global scoped or intent scoped mapper")
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
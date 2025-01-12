package net.asere.omni.result

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import net.asere.omni.core.ExecutionScope
import net.asere.omni.core.OmniHostDsl
import net.asere.omni.core.execute
import net.asere.omni.core.onError
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
        container.coroutineScope.launch(context = context) {
            if (scope.errorBlock != null) {
                scope.setResult(scope.errorBlock!!.invoke(it))
            } else if (container.exceptionMapper != null) {
                scope.setResult(container.exceptionMapper!!.valueOf(it))
            } else {
                throw RuntimeException(
                    "Exception mapper not set! Make sure you set " +
                            "the global scoped or intent scoped mapper"
                )
            }
        }
    }
    val job = execute(
        context = context,
        start = start,
        scope = scope,
    ) {
        scope.setResult(block())
    }

    return ResultIntent(
        job = job,
        scope = scope,
    )
}
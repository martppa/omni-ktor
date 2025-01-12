package net.asere.omni.ktor

import kotlinx.coroutines.CoroutineStart
import net.asere.omni.core.OmniHostDsl
import net.asere.omni.result.ResultIntent
import net.asere.omni.result.ResultScope
import net.asere.omni.result.constrainedIntent
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@OmniHostDsl
fun <Input, Result> ResponseContainerHost<Input, Result>.responseIntent(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend ResultScope<Response<Result>>.() -> Input
): ResultIntent<Response<Result>> {
    return constrainedIntent(
        context = context,
        start = start,
    ) {
        val result = block()
        container.responseMapper.valueOf(result)
    }
}
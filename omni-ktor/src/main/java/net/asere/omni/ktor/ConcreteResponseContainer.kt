package net.asere.omni.ktor

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import net.asere.omni.core.EmptyCoroutineExceptionHandler
import net.asere.omni.result.ConcreteConstrainedResultContainer
import net.asere.omni.result.ContentMapper
import kotlin.coroutines.EmptyCoroutineContext

class ConcreteResponseContainer<Result> internal constructor(
    override val responseMapper: ContentMapper<Result, Response<Result>>,
    exceptionMapper: ContentMapper<Throwable, Response<Result>>?,
    coroutineScope: CoroutineScope,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ConcreteConstrainedResultContainer<Response<Result>>(
    exceptionMapper = exceptionMapper,
    coroutineScope = coroutineScope,
    coroutineExceptionHandler = coroutineExceptionHandler,
), ResponseContainer<Result>

internal class DefaultResponse<Result> : ContentMapper<Result, Response<Result>> {
    override fun valueOf(input: Result): Response<Result> {
        return Response(code = 200, input)
    }
}

fun <Result> responseContainer(
    responseMapper: ContentMapper<Result, Response<Result>> = DefaultResponse(),
    exceptionMapper: ContentMapper<Throwable, Response<Result>>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    coroutineExceptionHandler: CoroutineExceptionHandler = EmptyCoroutineExceptionHandler
) = ConcreteResponseContainer(
    responseMapper = responseMapper,
    exceptionMapper = exceptionMapper,
    coroutineScope = coroutineScope,
    coroutineExceptionHandler = coroutineExceptionHandler,
)
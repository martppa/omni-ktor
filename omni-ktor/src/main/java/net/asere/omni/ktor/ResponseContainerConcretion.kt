package net.asere.omni.ktor

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import net.asere.omni.core.EmptyCoroutineExceptionHandler
import net.asere.omni.result.ConcreteConstrainedResultContainer
import kotlin.coroutines.EmptyCoroutineContext

class ConcreteResponseContainer<Input, Result> internal constructor(
    override val responseMapper: ResponseMapper<Input, Result>,
    exceptionMapper: ExceptionResponseMapper<Result>?,
    coroutineScope: CoroutineScope,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ConcreteConstrainedResultContainer<Response<Result>>(
    exceptionMapper = exceptionMapper,
    coroutineScope = coroutineScope,
    coroutineExceptionHandler = coroutineExceptionHandler,
), ResponseContainer<Input, Result>

fun <Input, Result> ResponseContainerHost<Input, Result>.responseContainer(
    responseMapper: ResponseMapper<Input, Result>,
    exceptionMapper: ExceptionResponseMapper<Result>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    coroutineExceptionHandler: CoroutineExceptionHandler = EmptyCoroutineExceptionHandler
) = ConcreteResponseContainer(
    responseMapper = responseMapper,
    exceptionMapper = exceptionMapper,
    coroutineScope = coroutineScope,
    coroutineExceptionHandler = coroutineExceptionHandler,
)
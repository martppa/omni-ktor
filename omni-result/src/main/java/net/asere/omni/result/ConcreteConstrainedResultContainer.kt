package net.asere.omni.result

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import net.asere.omni.core.EmptyCoroutineExceptionHandler
import kotlin.coroutines.EmptyCoroutineContext

open class ConcreteConstrainedResultContainer<Result>(
    override val exceptionMapper: ContentMapper<Throwable, Result>?,
    coroutineScope: CoroutineScope,
    coroutineExceptionHandler: CoroutineExceptionHandler
) : ConcreteResultContainer(
    coroutineScope = coroutineScope,
    coroutineExceptionHandler = coroutineExceptionHandler,
),  ConstrainedResultContainer<Result>

fun <Result> constrainedResultContainer(
    contentMapper: ContentMapper<Throwable, Result>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    coroutineExceptionHandler: CoroutineExceptionHandler = EmptyCoroutineExceptionHandler
) = ConcreteConstrainedResultContainer(
    exceptionMapper = contentMapper,
    coroutineScope = coroutineScope,
    coroutineExceptionHandler = coroutineExceptionHandler,
)
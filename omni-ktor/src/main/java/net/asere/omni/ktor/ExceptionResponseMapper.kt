package net.asere.omni.ktor

import net.asere.omni.result.ExceptionMapper

fun interface ExceptionResponseMapper<Output> : ExceptionMapper<Response<Output>> {
    override fun valueOf(input: Throwable): Response<Output>
}
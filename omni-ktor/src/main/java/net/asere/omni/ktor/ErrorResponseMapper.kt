package net.asere.omni.ktor

import net.asere.omni.result.ContentMapper

interface ErrorResponseMapper<Output> : ContentMapper<Throwable, Response<Output>> {
    override fun valueOf(input: Throwable): Response<Output>
}
package net.asere.omni.ktor

import net.asere.omni.result.ContentMapper

interface ResponseMapper<Input, Output> : ContentMapper<Input, Response<Output>> {
    override fun valueOf(input: Input): Response<Output>
}
package net.asere.omni.ktor

import net.asere.omni.result.ResultErrorMapper

interface ResponseMapper<T> : ResultErrorMapper<T, Response<T>> {
    override fun resultOf(input: T): Response<T>
}
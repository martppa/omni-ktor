package net.asere.omni.ktor

data class Response<T>(
    val code: Int,
    val body: T
)
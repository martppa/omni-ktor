package net.asere.omni.ktor

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val code: Int,
    val body: T
)
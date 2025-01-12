package net.asere.omni.ktor

import kotlinx.serialization.Serializable

private const val DefaultCode = 200

@Serializable
data class Response<T>(
    val code: Int = DefaultCode,
    val body: T
)
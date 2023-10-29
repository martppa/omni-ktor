package net.asere.omni.ktor.sample.model.errors

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
)
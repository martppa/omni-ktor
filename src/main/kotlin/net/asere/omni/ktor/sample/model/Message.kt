package net.asere.omni.ktor.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val value: String
)
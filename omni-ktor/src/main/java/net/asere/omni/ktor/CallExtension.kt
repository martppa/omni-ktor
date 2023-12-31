package net.asere.omni.ktor

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import net.asere.omni.result.ResultIntent

suspend inline fun <reified Result : Any> ApplicationCall.respond(block: () -> ResultIntent<Response<Result>>) {
    respond(block().awaitResult())
}

suspend inline fun <reified Result : Any> ApplicationCall.respond(response: Response<Result>) = respond(
    status = HttpStatusCode.fromValue(
        value = response.code
    ),
    message = response.body
)
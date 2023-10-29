package net.asere.omni.ktor.sample

import kotlinx.serialization.Serializable
import net.asere.omni.ktor.Response
import net.asere.omni.ktor.ResponseContainerHost
import net.asere.omni.ktor.responseContainer
import net.asere.omni.ktor.responseIntent
import net.asere.omni.ktor.sample.model.Message
import net.asere.omni.ktor.sample.model.errors.ApiError
import net.asere.omni.ktor.sample.model.errors.UserNotFoundError
import net.asere.omni.result.ContentMapper
import net.asere.omni.result.lambda
import net.asere.omni.result.onError

class ExampleController : ResponseContainerHost<@Serializable Any> {

    override val container = responseContainer(
        responseMapper = ExampleResponseMapper(),
        exceptionMapper = ExampleErrorMapper(),
    )

    fun message() = responseIntent {
        Message("This a 'happy path' response message provided from Omni-Ktor")
    }

    fun error500() = responseIntent {
        throw RuntimeException("Boom!")
    }

    fun errorUserNotFound() = responseIntent {
        throw UserNotFoundError()
    }

    private val localMapper = ContentMapper<Throwable, Response<Any>> { input ->
        when (input) {
            is IllegalStateException -> Response(code = 400, ApiError("bad_request"))
            else -> Response(code = 500, body = ApiError("internal_server_error"))
        }
    }

    fun overriddenError() = responseIntent {
        onError(localMapper.lambda)
        throw IllegalStateException()
    }

    fun noContent() = responseIntent {
        listOf<Any>()
    }
}
package net.asere.omni.ktor.sample

import kotlinx.coroutines.CoroutineExceptionHandler
import net.asere.omni.ktor.*
import net.asere.omni.ktor.sample.model.Message
import net.asere.omni.ktor.sample.model.errors.ApiError
import net.asere.omni.ktor.sample.model.errors.UserNotFoundError
import net.asere.omni.result.onError

class ExampleController : AnyResponseContainerHost {

    override val container = responseContainer(
        coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
            print(error)
        },
        responseMapper = ExampleResponseMapper(),
        exceptionMapper = ExampleExceptionMapper(),
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

    fun overriddenError() = responseIntent {
        onError {
            when (it) {
                is IllegalStateException -> Response(code = 400, ApiError("bad_request"))
                else -> Response(code = 500, body = ApiError("internal_server_error"))
            }
        }
        throw IllegalStateException()
    }

    fun noContent() = responseIntent {
        listOf<Any>()
    }
}
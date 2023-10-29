package net.asere.omni.ktor.sample

import net.asere.omni.ktor.Response
import net.asere.omni.ktor.ResponseMapper
import net.asere.omni.ktor.sample.model.errors.ApiError
import net.asere.omni.ktor.sample.model.errors.UserNotFoundError

class ExampleErrorMapper : ResponseMapper<Throwable, Any> {

    override fun valueOf(input: Throwable): Response<Any> {
        return when (input) {
            is UserNotFoundError -> Response(code = 404, body = ApiError("user_not_found"))
            else -> Response(code = 500, body = ApiError("internal_server_error"))
        }
    }
}
package net.asere.omni.ktor.sample

import net.asere.omni.ktor.Response
import net.asere.omni.ktor.ResponseMapper

class ExampleResponseMapper : ResponseMapper<Any, Any> {

    override fun valueOf(input: Any): Response<Any> {
        if (input is List<*> && input.isEmpty())
            return Response(code = 204, input)
        return Response(code = 200, input)
    }
}
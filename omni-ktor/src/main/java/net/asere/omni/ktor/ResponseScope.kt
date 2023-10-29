package net.asere.omni.ktor

import net.asere.omni.core.OmniHostDsl
import net.asere.omni.result.ResultScope

class ResponseScope<Result> : ResultScope<Response<Result>>() {
    internal var resultBlock: (suspend (Result) -> Response<Result>)? = null
}

@OmniHostDsl
fun <Result> ResponseScope<Result>.onResult(
    block: suspend (Result) -> Response<Result>
) {
    resultBlock = block
}
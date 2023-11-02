package net.asere.omni.ktor

import net.asere.omni.result.ConstrainedContainerHost

typealias AnyResponseContainerHost = ResponseContainerHost<Any, Any>

interface ResponseContainerHost<Input, Result> : ConstrainedContainerHost<Response<Result>> {
    override val container: ResponseContainer<Input, Result>
}
package net.asere.omni.ktor

import net.asere.omni.result.ConstrainedResultContainer
import net.asere.omni.result.ContentMapper

interface ResponseContainer<Input, Result> : ConstrainedResultContainer<Response<Result>> {
    val responseMapper: ResponseMapper<Input, Result>
}
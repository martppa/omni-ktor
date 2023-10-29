package net.asere.omni.ktor

import net.asere.omni.result.ConstrainedResultContainer
import net.asere.omni.result.ContentMapper

interface ResponseContainer<Result> : ConstrainedResultContainer<Response<Result>> {
    val responseMapper: ContentMapper<Result, Response<Result>>
}
package net.asere.omni.ktor

import net.asere.omni.result.ConstrainedContainerHost

interface ResponseContainerHost<Result> : ConstrainedContainerHost<Response<Result>> {
    override val container: ResponseContainer<Result>
}
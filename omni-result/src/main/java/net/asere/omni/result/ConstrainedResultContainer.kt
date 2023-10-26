package net.asere.omni.result

interface ConstrainedResultContainer<Result> : ResultContainer {
    val resultMapper: ResultErrorMapper<Throwable, Result>?
}
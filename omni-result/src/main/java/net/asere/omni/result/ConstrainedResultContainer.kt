package net.asere.omni.result

interface ConstrainedResultContainer<Result> : ResultContainer {
    val exceptionMapper: ContentMapper<Throwable, Result>?
}
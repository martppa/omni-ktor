package net.asere.omni.result

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first

class ConstrainedResultIntent<Result>(
    job: Job,
    scope: ResultScope<Result>,
    internal val container: ConstrainedResultContainer<Result>,
) : ResultIntent<Result>(job, scope)
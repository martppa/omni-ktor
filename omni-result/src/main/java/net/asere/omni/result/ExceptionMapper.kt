package net.asere.omni.result

fun interface ExceptionMapper<Output> : ContentMapper<Throwable, Output> {
    override fun valueOf(input: Throwable): Output
}
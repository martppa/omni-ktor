package net.asere.omni.result

interface ResultErrorMapper<Input, Output> {
    fun resultOf(input: Input): Output
}
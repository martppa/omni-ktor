package net.asere.omni.result

fun interface ContentMapper<Input, Output> {
    fun valueOf(input: Input): Output
}

val <Input, Output> ContentMapper<Input, Output>.lambda inline get(): (Input) -> Output {
    return ::valueOf
}
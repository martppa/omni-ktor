# omni-ktor ![](https://img.shields.io/badge/mvi_version-1.8.3-004475)
Omni Ktor is an [Omni-MVI](https://github.com/martppa/omni-mvi) core's feature which allows developers execute code and communicate its result back to Ktor. It follows the same principle of intents execution via containers, just like [Omni-MVI](https://github.com/martppa/omni-mvi)  

## Installation
In order to include omni-ktor, add the following dependencies to your project build.gradle file:
```groovy
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("net.asere.omni.mvi:core:$version")
    implementation("net.asere.omni.mvi:result:$version")
    implementation("net.asere.omni.mvi:ktor:$version")
}
```

## Setup
Communication with Ktor framework when using Omni-Ktor occurs via the `ResponseContainerHost` interface and its intents API. You can turn any class into a host by implementing `ResponseContainerHost` interface. To define a Ktor container host follow the syntax below:
```kotlin
class ExampleController : ResponseContainerHost<Any, Any> {

    override val container = responseContainer(
        exceptionMapper = ExampleErrorMapper(),
    )
    ...
}
```
Interfaces `Return` generic parameter is the type of data your host returns, the one to be delivered to the user, for example, what mappers return. `Input` generic parameter is the one your intents return. In many cases you would want this to be `<Any, Any` to allow you return any kind of type of data. For that you can use `AnyResponseContainerHost` which a type alias for `ResponseContainerHost<Any, Any>`

The `responseContainer()` builder function requires you to pass 4 arguments, 3 of them optionals:

- `coroutineScope`: Coroutine scope of intents execution. Defaulted to empty context coroutine scope
- `coroutineExceptionHandler`: The exception handler used to capture exception on intents execution, defaulted to an `EmptyCoroutineExceptionHandler`.
- `exceptionMapper`: Is going to be triggered every time an exception occurs. Captured exceptions are mapped and returned to Ktor framework as call answers. It's defaulted to null.
- `responseMapper`: Transforms intents returned values into responses.

### Custom Exception Mapper
As mentioned before, `ExceptionMapper` will translate any thrown error to a valid response. This will apply to any intent executed in the host. To define a custom exception mapper you just have to implement the `ExceptionMapper` interface:
```kotlin
class ExampleErrorMapper : ExceptionMapper<Any> {
    
    override fun valueOf(input: Throwable): Response<Any> {
        return when (input) {
            is UserNotFoundError -> Response(code = 404, body = ApiError("user_not_found"))
            else -> Response(code = 500, body = ApiError("internal_server_error"))
        }
    }
}
```

### Custom Response Mapper
`ResponseMapper` will do the same as `ExceptionMapper` but for any returned value from intents. Define a custom response mapper by implementing the `ResponseMapper` interface:
```kotlin
class ExampleResponseMapper : ResponseMapper<Any, Any> {
    
    override fun valueOf(input: Any): Response<Any> {
        if (input is List<*> && input.isEmpty())
            return Response(code = 204, input)
        return Response(code = 200, input)
    }
}
```

## Intents
Intents are suspending jobs running under a specific and controlled scope which grants access to possible thrown errors. Returned data at `responseIntent` will then be mapped using the `ResponseMapper`. To define a `responseIntent` you just have to follow the syntax bellow:
```kotlin
fun message() = responseIntent {
    Message("This a 'happy path' response message provided by Omni-Ktor")
}
```

### Intent scoped exception mapping
In case you prefer to override error mapper for a specific intent you can call `onError` top level extension and either provide an inline lambda declaration:
```kotlin
fun overriddenError() = responseIntent {
    onError {
        when (it) {
            is IllegalStateException -> Response(code = 400, ApiError("bad_request"))
            else -> Response(code = 500, body = ApiError("internal_server_error"))
        }
    }
    throw IllegalStateException()
}
```

You can also provide an `ExceptionResponseMapper` instance by calling its extension property `lambda`:
```kotlin
private val localMapper = ExceptionResponseMapper<Any> { input ->
    when (input) {
        is IllegalStateException -> Response(code = 400, ApiError("bad_request"))
        else -> Response(code = 500, body = ApiError("internal_server_error"))
    }
}

fun overriddenError() = responseIntent {
    onError(localMapper.lambda)
    throw IllegalStateException()
}
```

## Calling intents
To call intents you just have to use the top level extension function `respond` of Ktor's `ApplicationCall`:
```kotlin
fun start() = runBlocking {
    embeddedServer(Netty, port = 3000) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("example/message") {
                call.respond(controller::message)
            }
            get("example/error/internal") {
                call.respond(controller::error500)
            }
            get("example/error/user") {
                call.respond(controller::errorUserNotFound)
            }
            get("example/error/overridden") {
                call.respond(controller::overriddenError)
            }
            get("example/no-content") {
                call.respond(controller::noContent)
            }
        }
    }.start(wait = true)
}
```

# omni-result ![](https://img.shields.io/badge/mvi_version-1.8.3-004475)
Omni Result is an [Omni-MVI](https://github.com/martppa/omni-mvi) core's feature which allows the suspend execution of an intent to obtain its result.

## Installation
In order to include omni-result, add the following dependencies to your project build.gradle file:
```groovy
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("net.asere.omni.mvi:core:$version")
    implementation("net.asere.omni.mvi:result:$version")
}
```

## Standalone result host
Omni-Result allows to define standalone result containers. Standalone result containers let you execute result intents, these sort of intents handle their own result type. Every intent declared under a `ResultContainerHost` have their own result type. You can declare a `ResultContainerHost` using the following code:
```kotlin
class ExampleController(
    private val operationUseCase: OperationUseCase,
) : ResultContainerHost {
    override val container: ResultContainer = resultContainer()
    ...
}
```

### Standalone result intent
Use `resultIntent` top level extension to declare a result intent:
```kotlin
class ExampleController(
    private val operationUseCase: OperationUseCase,
) : ResultContainerHost {
    override val container = resultContainer()

    fun performOperation() = resultIntent {
        onError {
            OperationResult.Failed
        }
        operationUseCase()
        OperationResult.Success
    }
}
```

### Collecting intent's result
`resultIntent` execution returns a `ResultIntent` object. Use `awaitResult()` top level extension suspend function to await and retrieve the returning value. You can access the running `job` from the return intent.
```kotlin
val controller = ExampleController()
val operationIntent = controller.performOperation()
val result = operationIntent.awaitResult()
```

## Constrained result host
Just like `ResultContainerHost`, `ConstrainedResultContainerHost` allows you to execute `resultIntent` and `constrainedIntent`. Similar to `resultIntent`, `constraintIntent` returns a suspended result, but this one is bound to the container's result type. Use `ConstrainedResultContainerHost` when you need all intents in that host returns a common type. To declare a constrained host follow the code below:
```kotlin
class ExampleController(
    private val operationUseCase: OperationUseCase,
) : ConstrainedResultContainerHost<OperationReslt> {
    override val container = constrainedResultContainer()

    fun performOperation() = constrainedIntent {
        onError {
            OperationResult.Failed
        }
        operationUseCase()
        OperationResult.Success
    }
}
```

# License - MIT

Copyright 2023 Asere.net

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
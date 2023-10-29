package net.asere.omni.ktor.sample

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import net.asere.omni.ktor.respond

class Program {

    private val controller = ExampleController()

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
}
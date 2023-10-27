package net.asere.omni.ktor.sample

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

class Program {

    fun start() {
        embeddedServer(Netty, port = 3000) {
            routing {
                get("/message") { call.respond}
            }
        }
    }
}
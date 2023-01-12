package com.apptickar.plugins
import java.time.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*

fun Application.configureSockets() {
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
}

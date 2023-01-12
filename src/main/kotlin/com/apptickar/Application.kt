package com.apptickar

import com.apptickar.di.loadKoin
import io.ktor.server.application.*
import com.apptickar.plugins.*
import org.koin.ktor.plugin.Koin


fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

     configureKoin()
    configureSockets()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}

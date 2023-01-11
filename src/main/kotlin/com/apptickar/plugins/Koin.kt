package com.apptickar.plugins

import com.apptickar.di.loadKoin
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin()
{
    install(Koin)
    {
        loadKoin(environment)
    }
}
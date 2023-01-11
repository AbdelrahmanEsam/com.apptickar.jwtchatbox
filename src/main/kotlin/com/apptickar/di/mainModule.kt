package com.apptickar.di

import com.apptickar.data.sources.MongoUserDataSource
import com.apptickar.data.sources.UserDataSource
import com.apptickar.security.hashing.HashingService
import com.apptickar.security.hashing.SHA256HashingService
import com.apptickar.security.token.JWTTokenService
import com.apptickar.security.token.TokenConfig
import com.apptickar.security.token.TokenService
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import org.koin.core.KoinApplication
import org.koin.core.scope.get
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo



fun KoinApplication.loadKoin(environment: ApplicationEnvironment) : KoinApplication {

    val mainModule = module {
        single {
            KMongo.createClient().coroutine.getDatabase("userDatabase")
        }

        single<UserDataSource> {
            MongoUserDataSource(get())
        }

        single<TokenService> {
            JWTTokenService()
        }

        single {
            TokenConfig(
                issuer = environment.config.property("jwt.issuer").getString(),
                audience = environment.config.property("jwt.audience").getString(),
                expiresIn = 365L * 24L * 60L * 60L * 1000L,
                secret = System.getenv("JWT_SECRET") ?: "secret"
            )
        }

        single<HashingService>{
            SHA256HashingService()
        }

    }
    return modules(mainModule)
}



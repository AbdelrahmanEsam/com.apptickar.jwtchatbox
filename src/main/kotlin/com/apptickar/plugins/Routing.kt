package com.apptickar.plugins

import com.apptickar.data.sources.UserDataSource
import com.apptickar.room.RoomController
import com.apptickar.security.hashing.HashingService
import com.apptickar.security.token.TokenConfig
import com.apptickar.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.koin.ktor.ext.get
import routes.*

fun Application.configureRouting(hashingService: HashingService = get(), dataSource: UserDataSource = get(),
                                 tokenService: TokenService = get(), tokenConfig: TokenConfig = get(),roomController: RoomController = get()) {
    routing {
        chatSocket(roomController)
        getAllMessages(roomController)
          signUp(hashingService, dataSource)
         signIn(hashingService, dataSource, tokenService,tokenConfig)
        getInfo()
        authenticate()

    }
}

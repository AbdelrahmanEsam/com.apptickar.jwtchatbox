package com.apptickar.plugins

import com.apptickar.data.sources.MessageCollectionDataSource
import com.apptickar.data.sources.RoomDataSource
import com.apptickar.data.sources.UserDataSource
import com.apptickar.security.hashing.HashingService
import com.apptickar.security.token.TokenConfig
import com.apptickar.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import routes.*

fun Application.configureRouting(hashingService: HashingService = get(), userDataSource: UserDataSource = get(),
                                 tokenService: TokenService = get(), tokenConfig: TokenConfig = get(), roomDataSource: RoomDataSource = get()
                                 ,messageCollectionDataSource: MessageCollectionDataSource = get()) {
    routing {
        chatSocket(userDataSource = userDataSource,messageCollectionDataSource = messageCollectionDataSource,roomDataSource)
        getAllMessages(userDataSource,messageCollectionDataSource,roomDataSource)
        signUp(hashingService, userDataSource)
        signIn(hashingService, userDataSource, tokenService,tokenConfig)
        active(userDataSource)
        notActive(userDataSource)
        getAllChats(userDataSource,roomDataSource,messageCollectionDataSource)
        searchUser(userDataSource = userDataSource)
    }
}

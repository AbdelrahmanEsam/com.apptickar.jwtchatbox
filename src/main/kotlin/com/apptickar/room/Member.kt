package com.apptickar.room
import io.ktor.websocket.*
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val numberId: String,
    val socket: WebSocketSession
)

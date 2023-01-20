package com.apptickar.room
import com.apptickar.data.models.Message
import com.apptickar.data.models.MessageDataSourceModel
import com.apptickar.data.models.User
import com.apptickar.data.sources.MessageCollectionDataSource
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: MessageCollectionDataSource,
    private val roomId: String,
) {

    private val members = ConcurrentHashMap<String, Member>()

   fun onJoin(
        user: User,
        socket: WebSocketSession
    ) {
        if(members.containsKey(user.numberId)) {
            throw MemberAlreadyExistsException()
        }

        members[user.numberId] = Member(
            numberId = user.numberId,
            socket = socket
        )


    }

    suspend fun sendMessage(senderId: String ,message: String) {
        members.values.forEach { member ->
            val messageEntity = Message(
                text = message,
                username = senderId,
                timestamp = System.currentTimeMillis()
            )
            addNewMessageToDataSource(messageEntity)
            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return messageDataSource.getDataSource(roomId = roomId)?.messages?.sortedByDescending { it.timestamp } ?: emptyList()
    }

    suspend fun tryDisconnect(numberId: String) {
        members[numberId]?.socket?.close()
        if(members.containsKey(numberId)) {
            members.remove(numberId)
        }
    }

    private suspend fun addNewMessageToDataSource(newMessage : Message)
    {
        messageDataSource.addMessageToDataSource(message = newMessage,roomId)
    }
}
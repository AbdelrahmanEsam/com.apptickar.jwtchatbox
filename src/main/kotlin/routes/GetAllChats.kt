package routes

import com.apptickar.data.models.ChatState
import com.apptickar.data.sources.MessageCollectionDataSource
import com.apptickar.data.sources.RoomDataSource
import com.apptickar.data.sources.UserDataSource
import com.apptickar.data.util.timeFormatter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.concurrent.TimeUnit


fun Route.getAllChats(userDataSource: UserDataSource,roomDataSource : RoomDataSource,messageCollectionDataSource: MessageCollectionDataSource)
{
    authenticate {
        get("/getAllChats") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("UserId")?.asString()
            val users = userDataSource.getUser(userId!!) ?: return@get

          val chats =   getAllChatStates(users.first().numberId,roomDataSource,messageCollectionDataSource, userDataSource =  userDataSource)
            call.respond(
                HttpStatusCode.OK,
              chats
            )
        }
    }

}

private suspend  fun getAllChatStates(userId: String,roomDataSource: RoomDataSource,
                                      messageCollectionDataSource: MessageCollectionDataSource,userDataSource: UserDataSource) : Set<ChatState>
{
    val chatStates = mutableSetOf<ChatState>()
    val roomIds = getRoomsIds(userId, dataSource = userDataSource)
    println(roomIds.joinToString(" "))
    roomIds.forEach {roomId ->
      chatStates.add(getChatState(userId,roomId, roomDataSource, messageCollectionDataSource, userDataSource))
    }
    return chatStates
}

private suspend fun getChatState(userId: String,roomId : String,roomDataSource: RoomDataSource,
                                  messageCollectionDataSource: MessageCollectionDataSource,userDataSource: UserDataSource) : ChatState
{

    val room =  roomDataSource.getRoom(roomId)
    val member = room.members.first { it != userId }
    val user = userDataSource.getUser(member)!!.first()
    println(user.chatsSet)
    val messages  = messageCollectionDataSource.getDataSource(roomId)?.messages
    val lastSeen = (timeFormatter(System.currentTimeMillis()- messages!!.last().timestamp))
   return ChatState(numberId = user.numberId, lastMessage = messages.last().text,
    lastSeen =  lastSeen, unseenMessages = messages.count{ !it.seen }
    , isOnline = user.isOnline)
}

private suspend  fun getRoomsIds(userId : String,dataSource: UserDataSource) : MutableSet<String>
{
   val rooms =  dataSource.getUser(userId)?.first()?.chatsSet ?: mutableSetOf()
    return rooms
}
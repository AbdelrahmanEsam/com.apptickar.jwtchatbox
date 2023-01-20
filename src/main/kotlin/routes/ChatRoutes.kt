package routes

import com.apptickar.data.models.SendMessageDto
import com.apptickar.data.models.User
import com.apptickar.data.requests.GetRoomRequest
import com.apptickar.data.sources.MessageCollectionDataSource
import com.apptickar.data.sources.RoomDataSource
import com.apptickar.data.sources.UserDataSource
import com.apptickar.room.MemberAlreadyExistsException
import com.apptickar.room.RoomController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.pipeline.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

 var roomController : RoomController? = null
fun Route.chatSocket(userDataSource: UserDataSource,messageCollectionDataSource: MessageCollectionDataSource,roomDataSource: RoomDataSource) {

    authenticate {

        webSocket("/chat-socket") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("UserId")?.asString()
            val user = userDataSource.getUser(userId!!)?.first()


            if (user == null) {
                close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "no user found"))
                return@webSocket
            }


            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {

                        val message = Json.decodeFromString<SendMessageDto>(frame.readText())
                        if (roomController == null ) {
                            initRoomControllerAndUpdate(user.numberId,message.receiverId,userDataSource, messageCollectionDataSource, roomDataSource)
                        }

                        roomController?.onJoin(
                            user = user,
                            socket = this
                        )

                        roomController?.sendMessage(
                            senderId = user.numberId,
                            message = message.message
                        )
                    }
                }


            } catch (e: MemberAlreadyExistsException) {
                call.respond(HttpStatusCode.Conflict)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                roomController?.tryDisconnect(user.numberId)
                roomController = null
            }
        }
    }
    }


fun Route.getAllMessages(userDataSource: UserDataSource,messageCollectionDataSource: MessageCollectionDataSource,roomDataSource : RoomDataSource) {

authenticate {
     get("/messages") {
        val user = getAuthenticatedUser(userDataSource)

        val request = runCatching { call.receiveNullable<GetRoomRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
         println(minOf(request.receiverNumberId, user!!.numberId) + maxOf(request.receiverNumberId, user.numberId))
         if (roomController == null ) {
            initRoomControllerAndUpdate(user.numberId,request.receiverNumberId, userDataSource, messageCollectionDataSource, roomDataSource)
         }
        call.respond(
            HttpStatusCode.OK,
            roomController!!.getAllMessages()
        )
    }
  }
}

private suspend fun initRoomControllerAndUpdate(userId : String, receiverId : String, userDataSource: UserDataSource, messageCollectionDataSource: MessageCollectionDataSource, roomDataSource : RoomDataSource)
{

    coroutineScope {
        launch { roomController = getRoomById(minOf(receiverId, userId) + maxOf(receiverId, userId), messageCollectionDataSource,roomDataSource) }
            .invokeOnCompletion {
                launch(Dispatchers.IO) {  addNewRoomToUsers(minOf(receiverId, userId) + maxOf(receiverId ,userId), userDataSource, userId, receiverId)}
                launch(Dispatchers.IO){ roomDataSource.updateRoomMembers(newMember = userId, roomId = minOf(receiverId, userId) + maxOf(receiverId, userId))}
                launch(Dispatchers.IO){ roomDataSource.updateRoomMembers(newMember = receiverId, roomId = minOf(receiverId, userId) + maxOf(receiverId, userId))}
        }
    }
}

private suspend fun PipelineContext<Unit,ApplicationCall>.getAuthenticatedUser(userDataSource: UserDataSource) : User?
{
        val principal = call.principal<JWTPrincipal>()
        val userId = principal?.payload?.getClaim("UserId")?.asString()
        return userDataSource.getUser(userId!!)?.first()
}

private suspend fun getRoomById(roomId:String,messageCollectionDataSource: MessageCollectionDataSource,roomDataSource: RoomDataSource): RoomController {
     roomDataSource.getRoom(roomId)
    return RoomController(messageDataSource = messageCollectionDataSource, roomId = roomId)
}


private suspend fun addNewRoomToUsers(roomId : String , userDataSource: UserDataSource,senderId:String , receiverId : String)
{
    coroutineScope {
        launch(Dispatchers.IO) { updateUsersRooms(numberId = senderId, roomId, userDataSource) }
        launch(Dispatchers.IO) {  updateUsersRooms(numberId = receiverId, roomId, userDataSource)}
    }
}

private suspend fun updateUsersRooms(numberId : String , roomId: String, userDataSource: UserDataSource)
{
  userDataSource.updateUserChatRooms(numberId,roomId)

}
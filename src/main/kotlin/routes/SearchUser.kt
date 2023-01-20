package routes

import com.apptickar.data.responses.UserResponse
import com.apptickar.data.models.mapToUserResponse
import com.apptickar.data.requests.SearchRequest
import com.apptickar.data.sources.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.searchUser(userDataSource: UserDataSource)
{

    authenticate {
        get("/searchUser")
        {
            val request = runCatching { call.receiveNullable<SearchRequest>() }.getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            call.respond(HttpStatusCode.OK, searchUser(request.numberId,userDataSource))
        }
    }
}

suspend fun searchUser(numberId: String,userDataSource: UserDataSource) : List<UserResponse>
{
    return userDataSource.getUser(numberId)?.map { it.mapToUserResponse()  } ?: listOf(UserResponse("",""))
}
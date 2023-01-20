package routes

import com.apptickar.data.sources.UserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

fun Route.active(userDataSource: UserDataSource)
{
    authenticate {
        get("/active") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("UserId")?.asString()
            userId?.let {updateUser(it,userDataSource) }
           call.respond(HttpStatusCode.OK)
        }
    }
}

private suspend fun updateUser(userId : String,userDataSource: UserDataSource)
{
    coroutineScope {
        launch(Dispatchers.IO) {  userDataSource.updateUserState(userId,true)}
    }
}
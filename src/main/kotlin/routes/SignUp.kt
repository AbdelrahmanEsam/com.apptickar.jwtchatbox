package routes

import com.apptickar.data.models.User
import com.apptickar.data.requests.AuthRequest
import com.apptickar.data.sources.UserDataSource
import com.apptickar.security.hashing.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get


fun Route.signUp(hashingService: HashingService,dataSource: UserDataSource)
{
    post("/signUp")
    {

        val request = runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }


        if (request.username.isBlank() || request.password.isBlank())
        {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(request.username,saltedHash.hash,saltedHash.salt)

        val isAcknowledged = dataSource.insertNewUser(user)
        if (!isAcknowledged)
        {
            call.respond(HttpStatusCode.Conflict)
            return@post

        }
        call.respond(HttpStatusCode.OK)

    }
}
package routes

import com.apptickar.security.token.JWTTokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import routes.authenticate

fun Route.getInfo()
{
    authenticate {
        get("/info") {
             val principal = call.principal<JWTPrincipal>()
             val userId = principal?.getClaim("userId",String::class)
            call.respond(HttpStatusCode.OK,"hello $userId")
        }
    }
}
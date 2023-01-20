package routes

import com.apptickar.data.requests.AuthRequest
import com.apptickar.data.responses.AuthResponse
import com.apptickar.data.sources.UserDataSource
import com.apptickar.security.hashing.HashingService
import com.apptickar.security.hashing.SaltedHash
import com.apptickar.security.token.TokenClaim
import com.apptickar.security.token.TokenConfig
import com.apptickar.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Route.signIn(hashingService: HashingService, dataSource: UserDataSource,tokenService: TokenService,tokenConfig: TokenConfig)
{
    post("/signIn")
    {
        val request = runCatching { call.receiveNullable<AuthRequest>() }.getOrNull() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = dataSource.getUser(request.numberId)?.first()
        if (user == null)
        {
            call.respond(HttpStatusCode.Conflict,"incorrect username or password user null")
            return@post
        }
        val isValidPassword = hashingService.verify(request.password, SaltedHash(user.password,user.salt))

        if (!isValidPassword)
        {
            call.respond(HttpStatusCode.Conflict,"incorrect username or password invalid")
            return@post
        }

        println(user.numberId)

        val token = tokenService.generate(tokenConfig, TokenClaim("UserId",user.numberId))

        call.respond(status = HttpStatusCode.OK,AuthResponse(token))

    }

}

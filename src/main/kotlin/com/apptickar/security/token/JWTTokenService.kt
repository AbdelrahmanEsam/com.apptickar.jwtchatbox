package com.apptickar.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JWTTokenService : TokenService{

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
      val token = JWT.create().withAudience(config.audience).withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token.withClaim(claim.name,claim.value)
        }

        return token.sign(Algorithm.HMAC256(config.secret))
    }
}
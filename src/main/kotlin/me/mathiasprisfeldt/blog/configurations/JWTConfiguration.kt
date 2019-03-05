package me.mathiasprisfeldt.blog.configurations

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class JWTConfiguration(private val properties: BlogProperties,
                       private val userRepository: UserRepository) {

    private final val algorithm: Algorithm = Algorithm.HMAC256(properties.jwtSecret)

    val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(properties.jwtIssuer)
            .withAudience(properties.jwtAudience)
            .build()

    @Bean
    @Scope("prototype")
    fun create(userId: Long): String {
        return JWT
                .create()
                .withIssuer(properties.jwtIssuer)
                .withAudience(properties.jwtAudience)
                .withClaim("user-id", userId)
                .sign(algorithm)
    }

    @Bean
    @Scope("prototype")
    fun verify(token: String): DecodedJWT? {
        return try {
            verifier.verify(token)
        } catch (e: JWTVerificationException) {
            null
        }
    }

    @Bean
    @Scope("prototype")
    fun verifyRefresh(token: String): String? {
        val verified = verify(token) ?: return null
        val userId = verified.getClaim("user-id").asLong()

        val user: User = userRepository.findById(userId).orElse(null) ?: return null
        val newToken = create(user.id as Long)

        user.token = newToken
        userRepository.save(user)

        return newToken
    }
}
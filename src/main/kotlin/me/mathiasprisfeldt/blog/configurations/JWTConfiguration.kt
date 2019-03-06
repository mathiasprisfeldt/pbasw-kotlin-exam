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

    /**
     * The kind of algorithm the JWT generator uses.
     */
    private final val algorithm: Algorithm = Algorithm.HMAC256(properties.jwtSecret)

    /**
     * Builds a JWT verifier that uses the correct algorithm and
     * properties from BlogProperties
     */
    val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(properties.jwtIssuer)
            .withAudience(properties.jwtAudience)
            .build()

    /**
     * With a given user ID it creates a new JWT token and signs it.
     */
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

    /**
     * Uses the verifier to verify a specific token
     * It checks the algorithm and the secret.
     */
    @Bean
    @Scope("prototype")
    fun verify(token: String): DecodedJWT? {
        return try {
            verifier.verify(token)
        } catch (e: JWTVerificationException) {
            null
        }
    }

    /**
     * This acts both as a verifier and a refresher by
     * creating a new auth-token and returning it to the requester.
     */
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
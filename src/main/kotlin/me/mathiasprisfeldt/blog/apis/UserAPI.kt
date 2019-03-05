package me.mathiasprisfeldt.blog.apis

import me.mathiasprisfeldt.blog.configurations.JWTConfiguration
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.extensions.*
import me.mathiasprisfeldt.blog.repositories.UserRepository
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/user")
class UserAPI(private val userRepository: UserRepository,
              private val jwtConfiguration: JWTConfiguration) {

    @GetMapping("/")
    fun findAll() = userRepository.findAll()

    @GetMapping("/{username}")
    fun findOne(@PathVariable login: String) = userRepository.findByUsername(login)

    @PostMapping("/login")
    fun login(@RequestParam username: String,
              @RequestParam password: String,
              response: HttpServletResponse): JSONResponse {

        val user = userRepository.findByUsername(username)
                ?: return response.json(0, "Wrong username or password.")

        if (!user.login(password))
            return response.json(0, "Wrong username or password.")

        var token = user.token

        // Verify current token, it it still works use it.
        // Else create a new one.
        token = jwtConfiguration.verifyRefresh(token)
                ?: jwtConfiguration.create(user.id as Long)

        response.addCookie(token.authCookie())
        return response.json(HttpServletResponse.SC_ACCEPTED, "Logged in successfully.")
    }

    @PostMapping("/logout")
    fun logout(@CookieValue("auth-token") token: String,
               response: HttpServletResponse): JSONResponse {

        if (jwtConfiguration.verify(token) == null)
            return response.json(0, "Already logged out.")

        response.addCookie("".authCookie())
        return response.json(HttpServletResponse.SC_OK,"Logged out successfully.")
    }

    @PostMapping("/register")
    @ResponseBody
    fun register(@ModelAttribute form: RegistrationForm,
                 response: HttpServletResponse): JSONResponse {

        val valid = form.isValid()
        if (!valid.first)
            return response.json(0, valid.second)

        form.username?.run {
            if (userRepository.existsByUsername(this))
                return response.json(HttpServletResponse.SC_FORBIDDEN, "Username taken.")

            val user = form.toUser()
            val id = userRepository.save(user).id

            val jwt = jwtConfiguration.create(id as Long)
            user.token = jwt

            response.addCookie(jwt.authCookie())
            return response.json(HttpServletResponse.SC_ACCEPTED, "Success!")
        }

        return response.json(HttpServletResponse.SC_FORBIDDEN,
                "Failed to create user, try again later.")
    }

    @GetMapping("/validate")
    fun validate(@CookieValue("auth-token") token: String,
                 response: HttpServletResponse): DataJSONResponse<String> {

        val jwt = jwtConfiguration.verifyRefresh(token)
                ?: return response.dataJson(HttpServletResponse.SC_BAD_REQUEST, "Couldn't validate token")

        response.addCookie(jwt.authCookie())
        return response.dataJson(HttpServletResponse.SC_OK, "Successfully validated and refreshed token", jwt)
    }

    @GetMapping("/profile")
    fun profile(@CookieValue("auth-token") token: String,
                response: HttpServletResponse): DataJSONResponse<User> {
        val validate = validate(token, response)
        if (validate.status != HttpServletResponse.SC_OK)
            return response.dataJson(HttpServletResponse.SC_BAD_REQUEST, "Couldn't validate token")

        val decodedToken = jwtConfiguration.verify(validate.data.get())
        val id = decodedToken?.getClaim("user-id")?.asLong()
                ?: return response.dataJson(HttpServletResponse.SC_BAD_REQUEST, "Bad token.")

        val foundProfile = userRepository.findById(id)
        if (!foundProfile.isPresent)
            return response.dataJson(HttpServletResponse.SC_BAD_REQUEST, "Failed to find profile.")

        return response.dataJson(HttpServletResponse.SC_OK, data = foundProfile.get())
    }
}

data class RegistrationForm(
        val username: String?,
        val password: String?,
        val reEnterPassword: String?,
        val firstName: String?,
        val lastName: String?
) {
    fun isValid(): Pair<Boolean, String> {
        val errMsg: String = when {
            username == null -> "Username is missing."
            password == null -> "Password is missing."
            reEnterPassword == null -> "Re-entered password is missing."
            reEnterPassword != password -> "Password doesn't match"
            firstName == null -> "First name is missing."
            lastName == null -> "Last name is missing."
            else -> ""
        }

        return errMsg.isEmpty() to errMsg
    }

    fun toUser(): User = User(
            username as String,
            password as String,
            firstName as String,
            lastName as String)
}
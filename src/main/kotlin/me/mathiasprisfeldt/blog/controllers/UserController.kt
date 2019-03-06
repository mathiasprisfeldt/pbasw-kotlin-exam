package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.UserAPI
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@Controller
class UserController(private val userRepository: UserRepository,
                     private val userAPI: UserAPI) {

    /**
     * Shows all the users if authorized to see.
     */
    @GetMapping("/users")
    fun getUsers(@ModelAttribute("currUser") user: User?,
                 model: Model): String {
        if (user == null)
            return "redirect:/"

        model["users"] = userRepository.findAll()
        return "users"
    }

    /**
     * Shows the registration page.
     */
    @GetMapping("/register")
    fun getRegister(@ModelAttribute("currUser") user: User?,
                    model: Model): String {
        if (user != null)
            return "redirect:/"

        return "user/register"
    }

    /**
     * Called when an user submits a registration form.
     */
    @PostMapping("/register")
    fun postRegister(model: Model,
                     @ModelAttribute form: UserAPI.RegistrationForm,
                     response: HttpServletResponse): String {

        val formValidation = form.isValid()

        // If the form is invalid return the error message and return to registration page.
        if (!formValidation.first) {
            model["errMsg"] = formValidation.second
            return "user/register"
        }

        // If the form was valid but couldn't register in the API, return the error msg.
        val responseMsg = userAPI.register(form, response)
        if (responseMsg.status == HttpServletResponse.SC_FORBIDDEN) {
            model["errMsg"] = responseMsg.message
            return "user/register"
        }

        return "redirect:/"
    }

    /**
     * Shows the login screen.
     */
    @GetMapping("/login")
    fun getLogin(@ModelAttribute("currUser") user: User?,
                 model: Model) : String {
        if (user != null)
            return "redirect:/"

        return "user/login"
    }

    /**
     * Called when the user submits on the login page
     */
    @PostMapping("/login")
    fun postLogin(model: Model,
                  @RequestParam username: String,
                  @RequestParam password: String,
                  response: HttpServletResponse): String {

        // Uses the API to log the user in.
        val errMsg = userAPI.login(username, password, response)

        // If the API call returns an acceptable result redirect the user to home screen.
        if (errMsg.status == HttpServletResponse.SC_ACCEPTED)
            return "redirect:/"

        model["errMsg"] = errMsg.message
        return "user/login"
    }

    /**
     * Immediately log out the current user if any.
     */
    @GetMapping("/logout")
    fun getLogout(model: Model,
                  @CookieValue("auth-token") token: String?,
                  response: HttpServletResponse): String {
        if (token != null) {
            userAPI.logout(token, response)
        }

        return "redirect:/"
    }

    /**
     * If a user is logged in, it shows the users profile.
     */
    @GetMapping("/profile")
    fun getCurrUserProfile(@ModelAttribute("currUser") user: User?,
                           model: Model,
                           response: HttpServletResponse): String {
        if (user == null)
            return "redirect:/"

        return getProfile(user, user.username, model, response)
    }

    /**
     * Looks up a specific username and shows the corresponding profile page.
     */
    @GetMapping("/profile/{username}")
    fun getProfile(@ModelAttribute("currUser") user: User?,
                   @PathVariable username: String,
                   model: Model,
                   response: HttpServletResponse): String {

        // Unauthorized users isn't allowed to see profiles.
        if (user == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN)
            return ""
        }

        val foundUser = userRepository.findByUsername(username)
        if (foundUser != null) {
            model["user"] = foundUser
            model["articles"] = foundUser.articles
        }

        return "user/profile"
    }
}
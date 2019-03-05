package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.RegistrationForm
import me.mathiasprisfeldt.blog.apis.UserAPI
import me.mathiasprisfeldt.blog.entities.Article
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

    @GetMapping("/users")
    fun getUsers(@ModelAttribute("currUser") user: User?,
                 model: Model): String {
        if (user == null)
            return "redirect:/"

        model["users"] = userRepository.findAll()
        return "users"
    }

    @GetMapping("/register")
    fun getRegister(@ModelAttribute("currUser") user: User?,
                    model: Model): String {
        if (user != null)
            return "redirect:/"

        return "user/register"
    }

    @PostMapping("/register")
    fun postRegister(model: Model,
                     @ModelAttribute form: RegistrationForm,
                     response: HttpServletResponse): String {

        val formValidation = form.isValid()

        if (!formValidation.first) {
            model["errMsg"] = formValidation.second
            return "user/register"
        }

        val responseMsg = userAPI.register(form, response)
        if (responseMsg.status == HttpServletResponse.SC_FORBIDDEN) {
            model["errMsg"] = responseMsg.message
            return "user/register"
        }

        return "redirect:/"
    }

    @GetMapping("/login")
    fun getLogin(@ModelAttribute("currUser") user: User?,
                 model: Model) : String {
        if (user != null)
            return "redirect:/"

        return "user/login"
    }

    @PostMapping("/login")
    fun postLogin(model: Model,
                  @RequestParam username: String,
                  @RequestParam password: String,
                  response: HttpServletResponse): String {

        val errMsg = userAPI.login(username, password, response)

        if (errMsg.status == HttpServletResponse.SC_ACCEPTED)
            return "redirect:/"

        model["errMsg"] = errMsg.message
        return "user/login"
    }

    @GetMapping("/logout")
    fun getLogout(model: Model,
                  @CookieValue("auth-token") token: String?,
                  response: HttpServletResponse): String {
        if (token != null) {
            userAPI.logout(token, response)
        }

        return "redirect:/"
    }

    @GetMapping("/profile")
    fun getCurrUserProfile(@ModelAttribute("currUser") user: User?,
                           model: Model,
                           response: HttpServletResponse): String {
        if (user == null)
            return "redirect:/"

        return getProfile(user, user.username, model, response)
    }

    @GetMapping("/profile/{username}")
    fun getProfile(@ModelAttribute("currUser") user: User?,
                   @PathVariable username: String,
                   model: Model,
                   response: HttpServletResponse): String {

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
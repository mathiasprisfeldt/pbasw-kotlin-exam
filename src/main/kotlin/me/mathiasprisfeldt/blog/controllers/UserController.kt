package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.RegistrationForm
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

    @GetMapping("/users")
    fun getUsers(model: Model): String {
        model["users"] = userRepository.findAll()
        return "users"
    }

    @GetMapping("/register")
    fun getRegister(model: Model): String {
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

        return "redirect:/home"
    }

    @GetMapping("/login")
    fun getLogin(model: Model) : String {
        return "user/login"
    }

    @PostMapping("/login")
    fun postLogin(model: Model,
                  @RequestParam username: String,
                  @RequestParam password: String,
                  response: HttpServletResponse): String {

        val errMsg = userAPI.login(username, password, response)

        if (errMsg.status == HttpServletResponse.SC_ACCEPTED)
            return "redirect:/home"

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
                           model: Model): String {
        if (user == null)
            return "redirect:/"

        model["user"] = user
        return "user/profile"
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
        }

        return "user/profile"
    }
}
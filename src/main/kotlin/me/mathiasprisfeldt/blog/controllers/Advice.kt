package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.UserAPI
import me.mathiasprisfeldt.blog.configurations.BlogProperties
import me.mathiasprisfeldt.blog.entities.User
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class Advice(private val blogProperties: BlogProperties,
             private val userAPI: UserAPI) {

    @ModelAttribute("cfg")
    fun modelCfg(): BlogProperties.Model {
        return blogProperties.model
    }

    @ModelAttribute("currUser")
    fun loggedIn(@CookieValue("auth-token") token: String?,
                 response: HttpServletResponse): User? {
        if (token == null)
            return null

        return userAPI.profile(token, response)
    }
}
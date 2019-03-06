package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.UserAPI
import me.mathiasprisfeldt.blog.configurations.BlogProperties
import me.mathiasprisfeldt.blog.entities.User
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Used to settle global contracts used across API's and controllers.
 */
@ControllerAdvice
class Advice(private val blogProperties: BlogProperties,
             private val userAPI: UserAPI) {

    /**
     * Maps the model form the BlogProperties into a
     * model attribute.
     */
    @ModelAttribute("cfg")
    fun modelCfg(): BlogProperties.Model {
        return blogProperties.model
    }

    /**
     * Validates the current auth-token the user has
     * if it's valid the user is authorized to continue as
     * a logged in user.
     */
    @ModelAttribute("currUser")
    fun loggedIn(@CookieValue("auth-token") token: String?,
                 response: HttpServletResponse): User? {
        if (token == null)
            return null

        return userAPI.profile(token, response).data.orElse(null)
    }
}
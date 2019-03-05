package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.UserAPI
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import javax.servlet.http.HttpServletResponse

@Controller
class HomeController(
        private val articleRepository: ArticleRepository,
        private val userAPI: UserAPI) {

    @GetMapping("/", "/home")
    fun getHome(@ModelAttribute("currUser") user: User?,
                model: Model,
                response: HttpServletResponse): String {

        if (user != null) {
            val articles = articleRepository.findAllByOrderByAddedAtDesc()
            model["articles"] = articles
            return "home"
        }

        return "redirect:/login"
    }
}
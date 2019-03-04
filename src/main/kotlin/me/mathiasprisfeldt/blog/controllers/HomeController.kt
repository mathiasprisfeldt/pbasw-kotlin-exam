package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.BlogProperties
import me.mathiasprisfeldt.blog.entities.ArticleRepository
import me.mathiasprisfeldt.blog.entities.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable

@Controller
class HomeController(
        private val userRepository: UserRepository,
        private val articleRepository: ArticleRepository,
        private val properties: BlogProperties) {

    @ModelAttribute("cfg")
    fun CfgSetup(): BlogProperties.Model {
        return properties.model
    }

    @GetMapping("/")
    fun blog(model: Model): String {
        val articles = articleRepository.findAllByOrderByAddedAtDesc()
        model["articles"] = articles

        return "blog"
    }

    @GetMapping("/article/{slug}")
    fun article(@PathVariable slug: String, model: Model): String {
        val article = articleRepository
                .findBySlug(slug)
                ?: throw IllegalArgumentException("Wrong article slug provided")

        model["title"] = article.title
        model["article"] = article

        return "article_new"
    }

    @GetMapping("/users")
    fun users(model: Model): String {
        model["users"] = userRepository.findAll()
        return "users"
    }
}
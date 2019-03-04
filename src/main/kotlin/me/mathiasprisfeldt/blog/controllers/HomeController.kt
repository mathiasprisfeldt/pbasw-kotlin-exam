package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.entities.ArticleRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(
        private val articleRepository: ArticleRepository) {

    @GetMapping("/")
    fun getHome(model: Model): String {
        val articles = articleRepository.findAllByOrderByAddedAtDesc()
        model["articles"] = articles

        return "blog"
    }
}
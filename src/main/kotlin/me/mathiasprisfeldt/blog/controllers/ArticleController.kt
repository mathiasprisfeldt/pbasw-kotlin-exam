package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/article")
class ArticleController(private val articleRepository: ArticleRepository) {

    @GetMapping("/{slug}")
    fun getArticle(@ModelAttribute("currUser") user: User?,
                   @PathVariable slug: String,
                   model: Model): String {
        if (user == null) {
            return "redirect:/"
        }

        val article = articleRepository
                .findBySlug(slug)
                ?: throw IllegalArgumentException("Wrong article slug provided")

        model["title"] = article.title
        model["article"] = article

        return "article/article_new"
    }
}
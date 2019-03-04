package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.entities.ArticleRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ArticleController(private val articleRepository: ArticleRepository) {

    @GetMapping("/article/{slug}")
    fun getArticle(@PathVariable slug: String, model: Model): String {
        val article = articleRepository
                .findBySlug(slug)
                ?: throw IllegalArgumentException("Wrong article slug provided")

        model["title"] = article.title
        model["article"] = article

        return "article_new"
    }
}
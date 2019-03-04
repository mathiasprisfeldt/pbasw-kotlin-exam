package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.BlogProperties
import me.mathiasprisfeldt.blog.entities.ArticleRepository
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Repository
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class HtmlController(
        private val articleRepository: ArticleRepository,
        private val properties: BlogProperties) {

    fun Model.cfg() {
        this["cfg"] = properties.model
    }

    @GetMapping("/")
    fun blog(model: Model): String {
        model.cfg()

        val articles = articleRepository.findAllByOrderByAddedAtDesc()
        model["articles"] = articles

        return "blog"
    }

    @GetMapping("/article/{slug}")
    fun article(@PathVariable slug: String, model: Model): String {
        model.cfg()

        val article = articleRepository
                .findBySlug(slug)
                ?: throw IllegalArgumentException("Wrong article slug provided")

        model["title"] = article.title
        model["article"] = article

        return "article_new"
    }

}
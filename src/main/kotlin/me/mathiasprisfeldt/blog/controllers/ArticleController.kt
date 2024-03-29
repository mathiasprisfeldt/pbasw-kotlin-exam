package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.apis.ArticleAPI
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.extensions.toSlug
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/article")
class ArticleController(private val articleRepository: ArticleRepository,
                        private val articleAPI: ArticleAPI) {

    /**
     * Returns all the articles.
     */
    @GetMapping("")
    fun getArticles(model: Model,
                    @ModelAttribute("currUser") user: User?): String {

        model["articles"] = articleRepository.findAllByOrderByAddedAtDesc()

        return "article/articles"
    }

    /**
     * Returns a specific article by its slug.
     */
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

        model["article"] = article
        model["owner"] = article.author == user

        return "article/article_new"
    }

    /**
     * Gets called when the user presses delete on their article.
     */
    @PostMapping("/{slug}")
    fun postArticle(model: Model,
                    @ModelAttribute("currUser") user: User?,
                    @PathVariable slug: String,
                    response: HttpServletResponse): String {

        val result = articleAPI.remove(user, slug, response)

        if (result.status != HttpServletResponse.SC_OK) {
            model["errMsg"] = result.message
            return getArticle(user, slug, model)
        }

        return "redirect:/"
    }

    /**
     * Renders the site for creating a new article.
     */
    @GetMapping("/new")
    fun getNewArticle(@ModelAttribute("currUser") user: User?): String {

        if (user == null) {
            return "redirect:/"
        }

        return "article/create_article"
    }

    /**
     * Gets called when the users tries to create a new article.
     */
    @PostMapping("/new")
    fun postNewArticle(model: Model,
                       @ModelAttribute("currUser") user: User?,
                       @RequestParam("title") title: String,
                       @RequestParam("headline") headline: String,
                       @RequestParam("content") content: String,
                       response: HttpServletResponse): String {

        val article = articleAPI.create(
                user = user,
                title = title,
                headline = headline,
                content = content,
                response = response
        )

        if (article.status != HttpServletResponse.SC_OK) {
            model["errMsg"] = article.message
            return "article/create_article"
        }

        return "redirect:/article/${article.data.get()}"
    }

    /**
     * Renders the site for editing a specific article by
     * its slug.
     */
    @GetMapping("/edit/{slug}")
    fun getEditArticle(model: Model,
                       @PathVariable slug: String,
                       @ModelAttribute("currUser") user: User?,
                       response: HttpServletResponse): String {

        if (user == null) {
            return "redirect:/"
        }

        val article = articleRepository.findBySlug(slug) ?: return "redirect:/"
        model["article"] = article

        return "article/edit_article"
    }

    /**
     * Gets called when the user tries to save from the edit article page.
     * Successfully updates the article if the new information is valid.
     */
    @PostMapping("/edit/{slug}")
    fun postEditArticle(model: Model,
                        @ModelAttribute("currUser") user: User?,
                        @PathVariable("slug") slug: String,
                        @RequestParam("newTitle") newTitle: String,
                        @RequestParam("newHeadline") newHeadline: String,
                        @RequestParam("newContent") newContent: String,
                        response: HttpServletResponse): String {

        val result = articleAPI.edit(
                user,
                slug,
                newTitle,
                newHeadline,
                newContent,
                response
        )

        if (result.status != HttpServletResponse.SC_OK) {
            model["errMsg"] = result.message
            return getEditArticle(model, slug, user, response)
        }

        return "redirect:/article/${newTitle.toSlug()}"
    }
}
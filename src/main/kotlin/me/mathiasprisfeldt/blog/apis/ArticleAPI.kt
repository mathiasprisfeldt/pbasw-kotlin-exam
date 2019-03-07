package me.mathiasprisfeldt.blog.apis

import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.extensions.*
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/article")
class ArticleAPI(private val articleRepository: ArticleRepository) {

    @GetMapping("/")
    fun findAll() = articleRepository.findAllByOrderByAddedAtDesc()

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String) =
            articleRepository.findBySlug(slug) ?: throw IllegalArgumentException("Wrong article slug.")


    /**
     * Takes the 3 input parameters and creates a new article under the current user.
     */
    @PostMapping("/article/new")
    fun create(@ModelAttribute("currUser") user: User?,
               @RequestParam("title") title: String,
               @RequestParam("headline") headline: String,
               @RequestParam("content") content: String,
               response: HttpServletResponse): DataJSONResponse<String> {
        if (user == null)
            return response.dataJson(HttpServletResponse.SC_FORBIDDEN, "User not logged in.")

        val article = Article(
                title = title,
                headline = headline,
                content = content,
                author = user
        )

        if (articleRepository.existsBySlug(article.slug)) {
            return response.dataJson(HttpServletResponse.SC_BAD_REQUEST, "Title is taken.")
        }

        articleRepository.save(article)
        return response.dataJson(HttpServletResponse.SC_OK, "Successfully created new article.", article.slug)
    }

    /**
     * By a given slug as parameter it removes the article if the current
     * user is authorized.
     */
    @PostMapping("/article/remove")
    fun remove(@ModelAttribute("currUser") user: User?,
               @RequestParam("slug") slug: String,
               response: HttpServletResponse): JSONResponse {

        if (user == null)
            return response.json(HttpServletResponse.SC_FORBIDDEN, "User not logged in.")

        val article = articleRepository.findBySlug(slug)
                ?: return response.json(HttpServletResponse.SC_BAD_REQUEST, "Couldn't find article $slug")

        if (article.author != user)
            return response.json(HttpServletResponse.SC_FORBIDDEN, "${user.username} doesn't own this article.")

        articleRepository.delete(article)
        return response.json(HttpServletResponse.SC_OK, "Successfully deleted article $slug")
    }

    /**
     * Updates an existing article with new data if the user is authorized.
     */
    @PostMapping("/article/edit")
    fun edit(@ModelAttribute("currUser") user: User?,
             @RequestParam("slug") slug: String,
             @RequestParam("newTitle") newTitle: String,
             @RequestParam("newHeadline") newHeadline: String,
             @RequestParam("newContent") newContent: String,
             response: HttpServletResponse): DataJSONResponse<Article> {

        if (user == null)
            return response.dataJson(HttpServletResponse.SC_FORBIDDEN, "User not logged in.")

        val foundArticle = articleRepository.findBySlug(slug)
                ?: return response.dataJson(HttpServletResponse.SC_BAD_REQUEST, "Couldn't find article $slug")

        foundArticle.title = newTitle
        foundArticle.slug = newTitle.toSlug()
        foundArticle.headline = newHeadline
        foundArticle.content = newContent

        articleRepository.save(foundArticle)

        return response.dataJson(HttpServletResponse.SC_OK, "Successfully edited article.", foundArticle)
    }
}
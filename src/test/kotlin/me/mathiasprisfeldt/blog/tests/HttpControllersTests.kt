package me.mathiasprisfeldt.blog.tests

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import javafx.application.Application
import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.ArticleRepository
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.entities.UserRepository
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class HttpControllersTests(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var userRepository: UserRepository

    @MockkBean
    private lateinit var articleRepository: ArticleRepository

    val user = User("tonnibonde", "Tonni", "Bonde")

    val article1 = Article(
            title = "Tonni Bonde's Underværker",
            headline = "Du kan få kan du",
            content = "Jeg ved det sgu ikke du",
            author = user
    )

    val article2 = Article(
            title = "Tonni Bonde's Kogebog",
            headline = "Den helige gral",
            content = "Du skal bruge et barn eller to",
            author = user
    )

    @Test
    fun `List all articles`() {
        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(article1, article2)

        mockMvc.perform(get("/api/article/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("Tonni Bonde's Underværker")))
                .andExpect(content().string(containsString("Den helige gral")))
                .andExpect(content().string(containsString("Tonni Bonde's Kogebog")))
    }

    @Test
    fun `List all users`() {
        every { userRepository.findAll() } returns listOf(user)

        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("tonnibonde")))
                .andExpect(content().string(containsString("Tonni")))
                .andExpect(content().string(containsString("Bonde")))
    }
}
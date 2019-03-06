package me.mathiasprisfeldt.blog.tests

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.mathiasprisfeldt.blog.configurations.JWTConfiguration
import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import me.mathiasprisfeldt.blog.repositories.UserRepository
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.BeforeAll
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
    @MockkBean(relaxed = true) private lateinit var jwtConfiguration: JWTConfiguration
    @MockkBean(relaxed = true) private lateinit var userRepository: UserRepository
    @MockkBean(relaxed = true) private lateinit var articleRepository: ArticleRepository

    val user = User("testuser", "Test", "User", "")

    val article1 = Article(
            title = "Tonni Bonde's Marketing Tips",
            headline = "Bli' Anderkendt, hørt og stjålen fra",
            content = "Tonni nåede desværre ikke at skrive denne bog færdig da han har travlt" +
                    "ved at sælge en masse ting.",
            author = user
    )

    val article2 = Article(
            title = "Tonni Bonde's Kogebog",
            headline = "159 tips til hvordan du tilbereder en klageånd",
            content = "Læg låg på og ind i æ' ovn, nemt og renligt.",
            author = user
    )

    @Test
    fun `List all articles`() {
        every { articleRepository.findAllByOrderByAddedAtDesc() } returns listOf(article1, article2)

        mockMvc.perform(get("/api/article/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString(article1.title)))
                .andExpect(content().string(containsString(article2.title)))
    }

    @Test
    fun `List all users`() {
        every { userRepository.findAll() } returns listOf(user)

        mockMvc.perform(get("/api/user/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString(user.username)))
                .andExpect(content().string(containsString(user.firstName)))
                .andExpect(content().string(containsString(user.lastName)))
    }
}
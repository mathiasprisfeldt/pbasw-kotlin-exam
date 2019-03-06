package me.mathiasprisfeldt.blog.tests

import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import me.mathiasprisfeldt.blog.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.test.context.TestPropertySource

@Configuration
@TestPropertySource
class TestConfiguration {

    val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhcGkiLCJ1c2VyLWlkIjoxLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAifQ.l3LVWrjSLwna5heMbTSoNdYddC7XGAx8bfvEQiNqB40"
    val username = "testuser"
    val password = "password"

    fun interceptHeaders(template: TestRestTemplate) {
        template.restTemplate.interceptors.add(ClientHttpRequestInterceptor { request, body, execution ->
            request.headers.add("cookie", "auth-token=$token")
            execution.execute(request, body)
        })
    }

    @Bean
    fun testDatabaseIntializer(userRepository: UserRepository,
                           articleRepository: ArticleRepository) = ApplicationRunner {
        val user = User(username, password, "Test", "User")
        user.description = "Test user description."
        user.token = token
        userRepository.save(user)

        articleRepository.save(Article(
                title = "Tonni Bonde's Marketing Tips",
                headline = "Bli' Anderkendt, hørt og stjålen fra",
                content = "Tonni nåede desværre ikke at skrive denne bog færdig da han har travlt" +
                        "ved at sælge en masse ting.",
                author = user
        ))

        articleRepository.save(Article(
                title = "Tonni Bonde's Kogebog",
                headline = "159 tips til hvordan du tilbereder en klageånd",
                content = "Læg låg på og ind i æ' ovn, nemt og renligt.",
                author = user
        ))
    }
}
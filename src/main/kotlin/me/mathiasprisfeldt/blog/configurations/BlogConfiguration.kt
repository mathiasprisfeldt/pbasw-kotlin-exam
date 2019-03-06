package me.mathiasprisfeldt.blog.configurations

import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import me.mathiasprisfeldt.blog.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlogConfiguration {

    /**
     * Initializes the database with some preset data
     */
    @Bean
    fun databaseIntializer(userRepository: UserRepository,
                           articleRepository: ArticleRepository) = ApplicationRunner {
        val user = User("admin", "password", "Admin", "Jensen")
        user.description = "Server Administrator"
        userRepository.save(user)

        articleRepository.save(Article(
                title = "Hvordan du gør mit job lettere.",
                headline = "Få en masse visninger",
                content = "Egentlig burde der står en masse gejl om hvordan man gør en server" +
                        "administrator jobs nemmere, men kunne ikke finde på noget.",
                author = user
        ))
    }
}
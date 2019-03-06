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
        val user1 = User("admin", "password", "Admin", "Jensen")
        user1.description = "Server Administrator"
        userRepository.save(user1)

        val user2 = User("Tonni Bonde", "password", "Tonni", "Bonde")
        user2.description = "Bedste Salgsmand 1984"
        userRepository.save(user2)

        val user3 = User("Micki Lynge", "password", "Micki", "Lynge")
        user3.description = "Kan sit rullepølse kram"
        userRepository.save(user3)

        articleRepository.save(Article(
                title = "Hvordan du gør mit job lettere.",
                headline = "Få en masse visninger",
                content = "Egentlig burde der står en masse gejl om hvordan man gør en server" +
                        "administrator jobs nemmere, men kunne ikke finde på noget.",
                author = user1
        ))

        articleRepository.save(Article(
                title = "Hvordan flygter i en fart.",
                headline = "Der er ikke tid til headlines",
                content = "Køb dig nogle hurtige sko og briller og kom så afsted, den bliver" +
                        "ikke sværere.",
                author = user2
        ))

        articleRepository.save(Article(
                title = "Hvordan du tilbereder en rullepølse.",
                headline = "Mere salt",
                content = "Sørg for at kødet ikke er blevet for gammelt, og så lidt mere salt." +
                        "Husk er smage på det, og så noget mere salt.",
                author = user3
        ))
    }
}
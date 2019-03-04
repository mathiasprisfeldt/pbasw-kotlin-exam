package me.mathiasprisfeldt.blog

import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.ArticleRepository
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.entities.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlogConfiguration {

    @Bean
    fun databaseIntializer(userRepository: UserRepository,
                           articleRepository: ArticleRepository) = ApplicationRunner {
        val user = userRepository.save(User("tonnibonde", "Tonni", "Bonde"))

        articleRepository.save(Article(
                title = "Tonni Bonde's Underværker",
                headline = "Du kan få kan du",
                content = "Jeg ved det sgu ikke du",
                author = user
        ))

        articleRepository.save(Article(
                title = "Tonni Bonde's Kogebog",
                headline = "Den helige gral",
                content = "Du skal bruge et barn eller to",
                author = user
        ))
    }
}
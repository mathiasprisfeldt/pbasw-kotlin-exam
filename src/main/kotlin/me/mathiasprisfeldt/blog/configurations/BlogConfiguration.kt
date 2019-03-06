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

    @Bean
    fun databaseIntializer(userRepository: UserRepository,
                           articleRepository: ArticleRepository) = ApplicationRunner {
        val user = User("tonnibonde", "password", "Tonni", "Bonde")
        user.description = "Manden der kan sælge dig alt!"
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
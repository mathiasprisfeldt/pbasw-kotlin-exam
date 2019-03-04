package me.mathiasprisfeldt.blog.tests

import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.ArticleRepository
import me.mathiasprisfeldt.blog.entities.User
import me.mathiasprisfeldt.blog.entities.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired

@DataJpaTest
class RepositoriesTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val articleRepository: ArticleRepository) {

    @Test
    fun `When findByIdOrNull then return Article`() {
        val user = User(
                "testuser",
                "test",
                "user"
        )
        entityManager.persist(user)

        val article = Article(
                "Article title 1",
                "Headline",
                "Content",
                user
        )
        entityManager.persist(article)

        entityManager.flush()
        val found = articleRepository.findByIdOrNull(article.id!!)
        assertThat(found).isEqualTo(article)
    }

    @Test
    fun `When findByLogin then return User`() {
        val user = User(
                "testuser",
                "test",
                "user"
        )
        entityManager.persist(user)

        entityManager.flush()
        val foundUser = userRepository.findByLogin(user.login)
        assertThat(foundUser).isEqualTo(user)
    }
}
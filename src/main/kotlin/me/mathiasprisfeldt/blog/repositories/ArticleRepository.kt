package me.mathiasprisfeldt.blog.repositories

import me.mathiasprisfeldt.blog.entities.Article
import me.mathiasprisfeldt.blog.entities.User
import org.springframework.data.repository.CrudRepository

interface ArticleRepository : CrudRepository<Article, Long> {
    fun existsBySlug(slug: String): Boolean
    fun findBySlug(slug: String): Article?
    fun findByTitle(title: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
    fun findAllByAuthor(author: User): Iterable<Article>
}
package me.mathiasprisfeldt.blog.entities

import me.mathiasprisfeldt.blog.extensions.format
import me.mathiasprisfeldt.blog.extensions.toSlug
import me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.extensions.wordCount
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Article(
        var title: String,
        var headline: String,
        var content: String,
        @ManyToOne var author: User,
        var slug: String = title.toSlug(),
        var addedAt: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null) {

    val addedAtFormatted: String
        get() = addedAt.format()

    val wordCount: Int
        get() = content.wordCount()
}

interface ArticleRepository : CrudRepository<Article, Long> {
    fun findBySlug(slug: String): Article?
    fun findAllByOrderByAddedAtDesc(): Iterable<Article>
}
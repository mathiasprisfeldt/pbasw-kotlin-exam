package me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.entities.ArticleRepository
import me.mathiasprisfeldt.blog.entities.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/article")
class ArticleController(private val articleRepository: ArticleRepository) {

    @GetMapping("/")
    fun findAll() = articleRepository.findAllByOrderByAddedAtDesc()

    @GetMapping("/{slug")
    fun findOne(@PathVariable slug: String) =
            articleRepository.findBySlug(slug) ?: throw IllegalArgumentException("Wrong article slug.")
}

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/")
    fun findAll() = userRepository.findAll()

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String) = userRepository.findByLogin(login)
}
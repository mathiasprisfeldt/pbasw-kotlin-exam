package me.mathiasprisfeldt.blog.apis

import me.mathiasprisfeldt.blog.entities.UserRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserAPI(private val userRepository: UserRepository) {

    var loggedIn = false

    @GetMapping("/")
    fun findAll() = userRepository.findAll()

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String) = userRepository.findByLogin(login)

    @GetMapping("/login")
    fun login(): String {
        val msg = if (loggedIn) "Already logged in." else "Success!"
        loggedIn = true
        return msg
    }

    @GetMapping("/logout")
    fun logout(): String {
        val msg = if (!loggedIn) "Already logged out." else "Success!"
        loggedIn = false
        return msg
    }

    @GetMapping("/logged-in")
    fun loggedIn(): String {
        return loggedIn.toString()
    }
}
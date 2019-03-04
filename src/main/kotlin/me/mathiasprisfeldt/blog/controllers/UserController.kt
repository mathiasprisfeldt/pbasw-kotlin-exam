package me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.entities.UserRepository
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping

class UserController(private val userRepository: UserRepository) {

    @GetMapping("/users")
    fun getUsers(model: Model): String {
        model["users"] = userRepository.findAll()
        return "users"
    }
}
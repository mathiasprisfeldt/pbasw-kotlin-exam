package me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.BlogProperties
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute

@ControllerAdvice
class ControllerAdvice(private val blogProperties: BlogProperties) {

    @ModelAttribute("cfg")
    fun modelCfg(): BlogProperties.Model {
        return blogProperties.model
    }
}
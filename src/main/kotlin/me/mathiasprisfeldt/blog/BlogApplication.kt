package me.mathiasprisfeldt.blog

import me.mathiasprisfeldt.blog.configurations.BlogProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(BlogProperties::class)
class BlogApplication

fun main(args: Array<String>) {
	runApplication<BlogApplication>(*args)
}

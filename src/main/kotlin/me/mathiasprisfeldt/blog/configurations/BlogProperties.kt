package me.mathiasprisfeldt.blog.configurations

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("cfg")
class BlogProperties {
    lateinit var jwtSecret: String
    lateinit var jwtIssuer: String
    lateinit var jwtAudience: String

    val model = Model()

    class Model {
        lateinit var title: String
        lateinit var desc: String
        lateinit var author: String
        lateinit var copyright: String
    }
}
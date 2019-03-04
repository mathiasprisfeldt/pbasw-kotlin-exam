package me.mathiasprisfeldt.blog

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("cfg")
class BlogProperties {
    val model = Model()

    class Model {
        lateinit var title: String
        lateinit var desc: String
        lateinit var author: String
        lateinit var copyright: String
    }
}
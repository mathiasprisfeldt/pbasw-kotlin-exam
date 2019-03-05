package me.mathiasprisfeldt.blog.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
        var username: String,
        var password: Int,
        var firstName: String,
        var lastName: String,
        var description: String? = null,
        @Id @GeneratedValue var id: Long? = null) {

    var token: String = ""

    fun login(password: String): Boolean = password.hashCode() == this.password

    constructor(login: String, password: String, firstName: String, lastName: String) :
            this(login, password.hashCode(), firstName, lastName)
}
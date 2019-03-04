package me.mathiasprisfeldt.blog.entities

import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
        var login: String,
        var firstName: String,
        var lastName: String,
        var description: String? = null,
        @Id @GeneratedValue var id: Long? = null)

interface UserRepository : CrudRepository<User, Long> {
    fun findByLogin(login: String): User
}
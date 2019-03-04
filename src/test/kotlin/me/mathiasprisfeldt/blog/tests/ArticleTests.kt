package me.mathiasprisfeldt.blog.tests

import me.mathiasprisfeldt.blog.extensions.toSlug
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.springframework.http.HttpStatus
import org.springframework.web.util.HtmlUtils

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleTests(@Autowired val testRestTemplate: TestRestTemplate) {

    lateinit var dashboardBody: String

    @BeforeAll
    fun beforeAll() {
        val entity = testRestTemplate.getForEntity<String>("/")

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).isNotNull()

        dashboardBody = HtmlUtils.htmlUnescape(entity.body!!)
    }

    @Test
    fun `Assert that My Blog shows link to Tonni Bonde's Kogebog`() {
        assertThat(dashboardBody).contains("""
            <a href="/article/tonni-bonde-s-kogebog">Tonni Bonde's Kogebog</a>
        """.trimIndent())
    }

    @Test
    fun `Assert that article page for Tonni Bonde's Kogebog exists`() {
        val slug = "Tonni Bonde's Kogebog".toSlug()
        val entity = testRestTemplate.getForEntity<String>("/article/$slug")

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        val body = HtmlUtils.htmlUnescape(entity.body!!)
        assertThat(body).contains(
                "Den helige gral",
                "Du skal bruge et barn eller to",
                "Tonni Bonde's Kogebog"
        )
    }

    @Test
    fun `Assert that dashboard contains Tonni Bonde's Undervaerker`() {
        assertThat(dashboardBody).contains("""
            <a href="/article/tonni-bonde-s-underv-rker">Tonni Bonde's Underværker</a>
        """.trimIndent())
    }

    @Test
    fun `Assert that article page for Tonni Bonde's Undervearker exists`() {
        val slug = "Tonni Bonde's Underværker".toSlug()
        val entity = testRestTemplate.getForEntity<String>("/article/$slug")

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        val body = HtmlUtils.htmlUnescape(entity.body!!)
        assertThat(body).contains(
                "Du kan få kan du",
                "Jeg ved det sgu ikke du",
                "Tonni Bonde's Underværker"
        )
    }
}
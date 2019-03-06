package me.mathiasprisfeldt.blog.tests

import me.mathiasprisfeldt.blog.extensions.toSlug
import me.mathiasprisfeldt.blog.repositories.ArticleRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.util.HtmlUtils
import java.util.*
import java.util.Collections.singletonList

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleTests(@Autowired val testRestTemplate: TestRestTemplate,
                   @Autowired val config: TestConfiguration) {

    lateinit var dashboardBody: String

    @Autowired
    lateinit var articleRepository: ArticleRepository

    @LocalServerPort
    lateinit var port: String

    @BeforeAll
    fun beforeAll() {
        config.interceptHeaders(testRestTemplate)

        val homeEntity = testRestTemplate.getForEntity<String>("/")
        assertThat(homeEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(homeEntity.body).isNotNull()

        dashboardBody = HtmlUtils.htmlUnescape(homeEntity.body!!)
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
                "Tonni Bonde's Kogebog",
                "159 tips til hvordan du tilbereder en klageånd",
                "Læg låg på og ind i æ' ovn, nemt og renligt."
        )
    }

    @Test
    fun `Assert that dashboard contains Tonni Bonde's Marketing Tips`() {
        assertThat(dashboardBody).contains("""
            <a href="/article/tonni-bonde-s-marketing-tips">Tonni Bonde's Marketing Tips</a>
        """.trimIndent())
    }

    @Test
    fun `Assert that article page for Tonni Bonde's Marketing Tips exists`() {
        val slug = "Tonni Bonde's Marketing Tips".toSlug()
        val entity = testRestTemplate.getForEntity<String>("/article/$slug")

        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)

        val body = HtmlUtils.htmlUnescape(entity.body!!)
        assertThat(body).contains(
                "Bli' Anderkendt, hørt og stjålen fra",
                "Tonni nåede desværre ikke at skrive denne bog færdig da han har travlt" +
                        "ved at sælge en masse ting.",
                "Tonni Bonde's Marketing Tips"
        )
    }
}
package me.mathiasprisfeldt.blog.tests

import me.mathiasprisfeldt.blog.configurations.BlogProperties
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogApplicationTests(
		@Autowired val restTemplate: TestRestTemplate,
		@Autowired val config: TestConfiguration,
		@Autowired val properties: BlogProperties) {


	private lateinit var entity: ResponseEntity<String>

	@BeforeAll
	fun setup() {
		config.interceptHeaders(restTemplate)
		entity = restTemplate.getForEntity("/")
	}

	@Test
	fun `Assert content`() {
		assertThat(entity.body).contains("<h1>${properties.model.title}</h1>")
	}

	@Test
	fun `Assert status code`() {
		assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
	}

}

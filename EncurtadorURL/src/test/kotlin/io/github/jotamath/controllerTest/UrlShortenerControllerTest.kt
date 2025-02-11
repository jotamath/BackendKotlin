package io.github.jotamath.controllerTest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class UrlShortenerControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testShortenUrl() {
        val response = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://backendbrasil.com.br")
        )
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    fun testRedirect() {
        val shortenResponse = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://backendbrasil.com.br")
        )
        val shortCode = (shortenResponse.body() as Map<*, *>)["url"].toString().split("/").last()

        val redirectResponse = client.toBlocking().exchange<String>("/shorten-url/$shortCode")
        assertEquals(HttpStatus.OK, redirectResponse.status)
    }

    @Test
    fun `deve encurtar uma URL válida`() {
        val response = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://backendbrasil.com.br")
        )
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull((response.body() as Map<*, *>)["url"])
    }

    @Test
    fun `deve redirecionar para a URL original`() {
        // Encurta a URL
        val shortenResponse = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://backendbrasil.com.br")
        )
        val shortCode = (shortenResponse.body() as Map<*, *>)["url"].toString().split("/").last()

        // Redireciona usando o código encurtado
        val redirectResponse = client.toBlocking().exchange<String>("/shorten-url/$shortCode")
        assertEquals(HttpStatus.OK, redirectResponse.status)
    }

    @Test
    fun `deve retornar 404 para URL expirada`() {
        // Encurta a URL
        val shortenResponse = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://backendbrasil.com.br")
        )
        val shortCode = (shortenResponse.body() as Map<*, *>)["url"].toString().split("/").last()

        // Modifica a data de expiração no banco de dados (simula expiração)
        val expiredUrl = shortenedUrlRepository.findByShortCode(shortCode)!!
        expiredUrl.expirationDate = LocalDateTime.now().minusDays(1)
        shortenedUrlRepository.update(expiredUrl)

        // Tenta acessar a URL expirada
        val redirectResponse = client.toBlocking().exchange<String>("/shorten-url/$shortCode")
        assertEquals(HttpStatus.NOT_FOUND, redirectResponse.status)
    }

    @Test
    fun `deve retornar 404 para URL encurtada inválida`() {
        val redirectResponse = client.toBlocking().exchange<String>("/shorten-url/INVALIDO")
        assertEquals(HttpStatus.NOT_FOUND, redirectResponse.status)
    }

    @Test
    fun `deve retornar 400 para URL inválida`() {
        val response = client.toBlocking().exchange<Any>(
            "/shorten-url",
            mapOf("url" to "")
        )
        assertEquals(HttpStatus.BAD_REQUEST, response.status)
    }

    @Test
    fun `deve gerar códigos encurtados únicos`() {
        val response1 = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://backendbrasil.com.br")
        )
        val response2 = client.toBlocking().exchange(
            "/shorten-url",
            Map::class.java,
            mapOf("url" to "https://google.com")
        )
        val shortCode1 = (response1.body() as Map<*, *>)["url"].toString().split("/").last()
        val shortCode2 = (response2.body() as Map<*, *>)["url"].toString().split("/").last()
        assertNotEquals(shortCode1, shortCode2)
    }


}
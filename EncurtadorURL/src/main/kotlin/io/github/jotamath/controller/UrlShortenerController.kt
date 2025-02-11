package io.github.jotamath.controller

import io.github.jotamath.service.UrlShortenerService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpResponse.notFound
import io.micronaut.http.annotation.*
import java.net.URI // Import necessário para usar URI

@Controller("/shorten-url")
class UrlShortenerController(private val urlShortenerService: UrlShortenerService) {

    @Post
    fun shortenUrl(@Body request: Map<String, String>): HttpResponse<Map<String, String>> {
        val originalUrl = request["url"]!!
        val shortenedUrl = urlShortenerService.shortenUrl(originalUrl)
        return HttpResponse.ok(mapOf("url" to shortenedUrl))
    }

    @Get("/{shortCode}")
    fun redirect(@PathVariable shortCode: String): HttpResponse<*> {
        val originalUrl = urlShortenerService.getOriginalUrl(shortCode)
        return if (originalUrl != null) {
            HttpResponse.redirect<Any>(URI.create(originalUrl)) // Especifica o tipo <Any>
        } else {
            notFound()
        }
    }
}
package io.github.jotamath.service

import io.github.jotamath.model.ShortenedUrl
import io.github.jotamath.repository.ShortenedUrlRepository
import jakarta.inject.Singleton
import java.time.LocalDateTime
import java.util.*

@Singleton
class UrlShortenerService(private val shortenedUrlRepository: ShortenedUrlRepository) {

    fun shortenUrl(originalUrl: String): String {
        // Gera um código encurtado único
        val shortCode = generateShortCode()

        // Define a data de expiração (ex: 7 dias a partir de agora)
        val expirationDate = LocalDateTime.now().plusDays(7)

        // Salva no banco de dados
        val shortenedUrl = ShortenedUrl(
            originalUrl = originalUrl,
            shortCode = shortCode,
            expirationDate = expirationDate
        )
        shortenedUrlRepository.save(shortenedUrl)

        // Retorna a URL encurtada
        return "https://xxx.com/$shortCode"
    }

    private fun generateShortCode(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun getOriginalUrl(shortCode: String): String? {
        val shortenedUrl = shortenedUrlRepository.findByShortCode(shortCode)
        return if (shortenedUrl != null && shortenedUrl.expirationDate.isAfter(LocalDateTime.now())) {
            shortenedUrl.originalUrl
        } else {
            null
        }
    }
}
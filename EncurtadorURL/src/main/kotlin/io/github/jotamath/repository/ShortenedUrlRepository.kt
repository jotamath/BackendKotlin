package io.github.jotamath.repository

import io.github.jotamath.model.ShortenedUrl
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository


@Repository
interface ShortenedUrlRepository : JpaRepository<ShortenedUrl, Long> {
    fun findByShortCode(shortCode: String) : ShortenedUrl?
}
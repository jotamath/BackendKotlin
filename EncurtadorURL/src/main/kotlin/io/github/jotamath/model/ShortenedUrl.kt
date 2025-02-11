package io.github.jotamath.model
import javax.persistence.*
import java.time.LocalDateTime

@Entity
data class ShortenedUrl(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val originalUrl: String,

    @Column(nullable = false, unique = true)
    val shortCode: String,

    @Column(nullable = false)
    val expirationDate: LocalDateTime
)

package co.digamma.ca.domain.api.common

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

@JvmInline
value class Timestamp private constructor(val value: Instant) {
    companion object {
        fun now(): Timestamp = of(Instant.now())
        fun of(value: Instant): Timestamp = Timestamp(value.truncatedTo(ChronoUnit.MILLIS))
        fun of(value: ZonedDateTime): Timestamp = of(value.toInstant())
        fun of(value: LocalDateTime): Timestamp = of(value.atZone(ZoneId.systemDefault()))
    }
}
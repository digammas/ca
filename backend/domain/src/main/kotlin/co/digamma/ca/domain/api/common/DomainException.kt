package co.digamma.ca.domain.api.common

open class DomainException(
    message: String?,
    cause: Throwable? = null
) : RuntimeException(message, cause)


package co.digamma.ca.domain.api.common

class BadInput(
    message: String?,
    cause: Throwable? = null,
) : DomainException(message, cause)
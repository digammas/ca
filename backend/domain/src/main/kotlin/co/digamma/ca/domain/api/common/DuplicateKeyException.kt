package co.digamma.ca.domain.api.common

class DuplicateKeyException(
    message: String?,
    cause: Throwable? = null
): DomainException(message, cause) {
    companion object {
        fun of(kass: Class<*>, id: String, cause: Throwable? = null): NotFoundException {
            val message = "${kass.simpleName} already exists with ID $id"
            return NotFoundException(message, cause)
        }

        fun of(id: String, cause: Throwable? = null): NotFoundException {
            val message = "Duplicate ID $id"
            return NotFoundException(message, cause)
        }
    }
}
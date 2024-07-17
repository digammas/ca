package co.digamma.ca.domain.api.common

class NotFoundException(
    message: String?,
    cause: Throwable? = null
) : DomainException(message, cause) {

    companion object {
        fun of(kass: Class<*>, id: String, cause: Throwable? = null): NotFoundException {
            val message = "${kass.simpleName} with ID $id not found"
            return NotFoundException(message, cause)
        }

        fun of(id: String, cause: Throwable? = null): NotFoundException {
            val message = "Model with ID $id not found"
            return NotFoundException(message, cause)
        }
    }
}
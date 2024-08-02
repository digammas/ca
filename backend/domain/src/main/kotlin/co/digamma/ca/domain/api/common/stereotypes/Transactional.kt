package co.digamma.ca.domain.api.common.stereotypes

annotation class Transactional(
    /**
     * Whether the transaction is read-only.
     */
    val readOnly: Boolean = false,
    /**
     * Whether a nested transaction should be created.
     */
    val nested: Boolean = false,
)

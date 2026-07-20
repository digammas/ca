package co.digamma.ca.domain.api.common

data class Page<T>(
    val results: List<T>,
    val index: Int,
    val size: Int,
    val totalPages: Int,
) {
    constructor(
        results: List<T>,
        index: Int,
        totalPages: Int,
    ) : this(results, index, results.size, totalPages)

    fun <R> map(mapper: (T) -> R): Page<R> = Page(results.map(mapper), index, size, totalPages)
}


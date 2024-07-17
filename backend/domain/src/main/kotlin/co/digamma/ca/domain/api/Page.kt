package co.digamma.ca.domain.api

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
}

data class PageSpecs(
    val index: Int,
    val size: Int,
)


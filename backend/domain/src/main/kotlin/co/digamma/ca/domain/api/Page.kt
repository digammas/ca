package co.digamma.ca.domain.api

data class Page<T>(
    val results: List<T>,
    val index: Int,
    val size: Int,
    val totalPages: Int,
)

data class PageSpecs(
    val index: Int,
    val size: Int,
)


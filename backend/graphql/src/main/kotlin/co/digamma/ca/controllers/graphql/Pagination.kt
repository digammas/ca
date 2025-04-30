package co.digamma.ca.controllers.graphql

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import graphql.relay.Connection
import graphql.relay.DefaultConnection
import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo

data class PageQuery(
    val first: Int,
    val from: String,
) {
    fun asSpecs(): PageSpecs {
        val size = if (first == 0) 10 else first
        val index = (from.toIntOrNull() ?: 0) / size
        return PageSpecs(index, size)
    }
}

private fun Int.toCursor() = DefaultConnectionCursor(this.toString())

fun <T> Page<T>.asConnection(): Connection<T> {
    return this.asConnection { x -> x }
}

fun <T, R> Page<T>.asConnection(mapper: (T) -> R): Connection<R> {
    return DefaultConnection(
        this.results.mapIndexed { index, item ->
            DefaultEdge(mapper(item), index.toCursor())
        },
        DefaultPageInfo(
            (this.index * this.size).toCursor(),
            ((this.index + 1) * this.size).toCursor(),
            this.index != 0,
            this.index != this.totalPages - 1
        )
    )
}

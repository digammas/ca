package co.digamma.ca.controllers.graphql

import co.digamma.ca.domain.api.Page
import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.model.Model
import graphql.relay.Connection
import graphql.relay.DefaultConnection
import graphql.relay.DefaultConnectionCursor
import graphql.relay.DefaultEdge
import graphql.relay.DefaultPageInfo

data class PageQuery(
    val first: Int = 10,
    val from: Int = 0,
) {
    fun asPageSpecs(): PageSpecs {
        val size = if (first == 0) 10 else first
        val index = from / size
        return PageSpecs(index, size)
    }
}

private fun Int.toCursor() = DefaultConnectionCursor(this.toString())

fun <T: Model> Page<T>.asConnection(): Connection<T> {
    return DefaultConnection(
        this.results.mapIndexed { index, item -> DefaultEdge(item, index.toCursor()) },
        DefaultPageInfo(
            (this.index * this.size).toCursor(),
            ((this.index + 1) * this.size).toCursor(),
            this.index != 0,
            this.index != this.totalPages - 1
        )
    )
}
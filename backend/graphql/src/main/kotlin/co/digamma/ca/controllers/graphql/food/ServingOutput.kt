package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.Serving

data class ServingOutput(
    val id: String,
    val locale: String,
    val name: String,
)

fun Serving.toServingOutput() = ServingOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
)

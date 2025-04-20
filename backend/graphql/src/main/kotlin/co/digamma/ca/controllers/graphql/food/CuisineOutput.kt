package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.Cuisine

data class CuisineOutput(
    val id: String,
    val locale: String,
    val name: String,
)

fun Cuisine.toCuisineOutput() = CuisineOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
)

package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.Serving

data class ServingOutput(
    val id: String,
    val locale: String,
    val name: String,
    val temperatureMin: Int,
    val temperatureMax: Int,
)

fun Serving.toServingOutput() = ServingOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
    temperatureMin = this.temperature.min(),
    temperatureMax = this.temperature.max(),
)

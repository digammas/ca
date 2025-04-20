package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.ServingCreation
import java.util.Locale


data class ServingCreationInput(
    private val locale: String,
    private val name: String,
    private val temperatureMin: Int? = null,
    private val temperatureMax: Int? = null,
) {

    fun toServingCreation() = ServingCreation(
        locale = Locale.forLanguageTag(this.locale),
        name = this.name,
        temperatureMin?.rangeTo(temperatureMax ?: temperatureMin),
    )
}

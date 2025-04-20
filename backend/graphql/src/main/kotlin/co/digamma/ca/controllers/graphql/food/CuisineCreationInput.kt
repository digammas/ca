package co.digamma.ca.controllers.graphql.food

import co.digamma.ca.domain.api.food.CuisineCreation
import java.util.Locale


data class CuisineCreationInput(
    private val locale: String,
    private val name: String
) {

    fun toCuisineCreation() = CuisineCreation(
        locale = Locale.forLanguageTag(this.locale),
        name = this.name,
    )
}

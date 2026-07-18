package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.IngredientCreation
import java.util.Locale


data class IngredientCreationInput(
    private val locale: String,
    private val name: String,
    private val description: String,
    private val images: List<String>?,
) {

    fun toIngredientCreation() = IngredientCreation(
        locale = Locale.forLanguageTag(this.locale),
        name = this.name,
        description = this.description,
        imageIds = this.images ?: emptyList(),
    )
}

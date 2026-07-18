package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.controllers.graphql.media.ImageOutput
import co.digamma.ca.controllers.graphql.media.toImageOutput
import co.digamma.ca.domain.api.cookbook.Ingredient

data class IngredientOutput(
    val id: String,
    val locale: String,
    val name: String,
    val description: String,
    val images: List<ImageOutput>,
)

fun Ingredient.toIngredientOutput() = IngredientOutput(
    id = this.id,
    locale = this.locale.toString(),
    name = this.name,
    description = this.description,
    images = this.images.map { it.toImageOutput() },
)

package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.RecipeCreation
import java.util.Locale

data class RecipeCreationInput(
    private val locale: String,
    private val dishId: String,
    private val yield: Int,
    private val images: List<String>?,
    private val timeToServe: Int?,
    private val author: String,
    private val steps: List<StepCreationInput>?,
    private val ingredients: List<QuantifiedIngredientCreationInput>?,
) {

    fun toRecipeCreation() = RecipeCreation(
        locale = Locale.forLanguageTag(this.locale),
        dishId = this.dishId,
        yield = this.yield,
        imageIds = this.images ?: emptyList(),
        timeToServe = this.timeToServe ?: 0,
        author = this.author,
        steps = this.steps?.map { it.toStepCreation() } ?: emptyList(),
        ingredients = this.ingredients?.map { it.toQuantifiedIngredientCreation() } ?: emptyList(),
    )
}

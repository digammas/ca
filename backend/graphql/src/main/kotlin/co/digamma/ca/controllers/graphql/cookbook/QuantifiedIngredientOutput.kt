package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient

data class QuantifiedIngredientOutput(
    val id: String,
    val ingredient: IngredientOutput,
    val quantity: Float,
    val unit: MeasurementUnitOutput,
    val recipe: RecipeOutput,
)

fun QuantifiedIngredient.toQuantifiedIngredientOutput() = QuantifiedIngredientOutput(
    id = this.id,
    ingredient = this.ingredient.toIngredientOutput(),
    quantity = this.quantity,
    unit = this.unit.toMeasurementUnitOutput(),
    recipe = this.recipe.toRecipeOutput(),
)

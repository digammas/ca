package co.digamma.ca.controllers.graphql.cookbook

import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientCreation

data class QuantifiedIngredientCreationInput(
    private val recipeId: String?,
    private val ingredientId: String,
    private val measurementUnitId: String,
    private val quantity: Float,
    private val order: Int?,
) {

    fun toQuantifiedIngredientCreation() = QuantifiedIngredientCreation(
        recipeId = this.recipeId,
        ingredientId = this.ingredientId,
        measurementUnitId = this.measurementUnitId,
        quantity = this.quantity,
        order = this.order,
    )
}

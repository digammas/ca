package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.model.Model

data class QuantifiedIngredient(
    override val id: String,
    val ingredient: Ingredient,
    val quantity: Float,
    val unit: MeasurementUnit,
    val recipe: Recipe,
) : Model

package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.model.Model

class RecipeIngredient(
    override val id: String,
    val recipe: Recipe,
    val ingredient: Ingredient,
    val quantity: Float,
    val unit: MeasurementUnit,
): Model

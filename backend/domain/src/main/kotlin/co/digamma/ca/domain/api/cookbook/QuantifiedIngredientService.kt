package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.model.ModelReference

interface QuantifiedIngredientService :
    CrudService<QuantifiedIngredient, QuantifiedIngredientCreation, QuantifiedIngredientModification> {

    fun retrieveByRecipe(recipeId: String): List<QuantifiedIngredient>
}

interface QuantifiedIngredientMutation {
    val measurementUnitId: String?
    val quantity: Float?
    val order: Int?
}

data class QuantifiedIngredientCreation(
    val recipeId: String? = null,
    val ingredientId: String,
    override val measurementUnitId: String,
    override val quantity: Float,
    override val order: Int? = null,
) : QuantifiedIngredientMutation

data class QuantifiedIngredientModification(
    override val id: String,
    override val measurementUnitId: String? = null,
    override val quantity: Float? = null,
    override val order: Int? = null,
) : QuantifiedIngredientMutation, ModelReference<QuantifiedIngredient>


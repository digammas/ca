package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.model.ModelReference
import java.util.Locale

interface RecipeService : CrudService<Recipe, RecipeCreation, RecipeModification>

interface RecipeMutation {
    val dishId: String?
    val yield: Int?
    val imageIds: List<String>
    val timeToServe: Int?
}

data class RecipeCreation(
    val locale: Locale,
    override val dishId: String,
    override val yield: Int,
    override val imageIds: List<String> = emptyList(),
    override val timeToServe: Int?,
    val author: String,
    val steps: List<StepCreation>,
    val ingredients: List<QuantifiedIngredientCreation>,
) : RecipeMutation

data class RecipeModification(
    override val id: String,
    override val dishId: String?,
    override val yield: Int?,
    override val imageIds: List<String> = emptyList(),
    override val timeToServe: Int?,
) : RecipeMutation, ModelReference<Recipe>

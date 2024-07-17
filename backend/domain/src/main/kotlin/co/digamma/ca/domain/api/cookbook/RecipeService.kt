package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService

interface RecipeService : RetrieveService<Recipe>, DeleteService {
    fun create(recipe: RecipeCreation): Recipe
    fun update(recipe: RecipeModification): Recipe
}

data class PreparationStepCreation(
    val description: String,
    val estimatedTime: Int,
    val order: Int? = null,
)

data class QuantifiedIngredientCreation(
    val ingredientId: String,
    val measurementUnitId: String,
    val quantity: Float,
    val order: Int? = null,
)

interface RecipeMutation {
    val dishId: String?
    val steps: List<PreparationStepCreation>?
    val ingredients: List<QuantifiedIngredientCreation>?
    val yield: Int?
    val imageIds: List<String>
    val estimatedTime: Int?
}

data class RecipeCreation(
    val locale: String,
    override val dishId: String,
    override val steps: List<PreparationStepCreation>,
    override val ingredients: List<QuantifiedIngredientCreation>,
    override val yield: Int,
    override val imageIds: List<String> = emptyList(),
    override val estimatedTime: Int?,
) : RecipeMutation

data class RecipeModification(
    val id: String,
    override val dishId: String?,
    override val steps: List<PreparationStepCreation>?,
    override val ingredients: List<QuantifiedIngredientCreation>?,
    override val yield: Int?,
    override val imageIds: List<String> = emptyList(),
    override val estimatedTime: Int?,
    val addedSteps: List<PreparationStepCreation>?,
    val removedStepIds: List<String>?,
    val updatedIngredients: List<QuantifiedIngredientCreation>?,
) : RecipeMutation {
    init {
        require(steps == null || (addedSteps == null && removedStepIds == null)) {
            "Cannot add or remove steps when updating existing steps"
        }
        require(ingredients == null || updatedIngredients == null) {
            "Cannot update existing ingredients when adding or removing ingredients"
        }
    }
}

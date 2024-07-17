package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.DeleteService
import co.digamma.ca.domain.api.RetrieveService

interface IngredientService : RetrieveService<Ingredient>, DeleteService {
    fun create(ingredient: IngredientCreation): Ingredient
    fun update(ingredient: IngredientModification): Ingredient
}

interface IngredientMutation {
    val name: String?
    val description: String?
    val imageIds: List<String>?
}

data class IngredientCreation(
    val locale: String,
    override val name: String,
    override val description: String,
    override val imageIds: List<String> = emptyList(),
) : IngredientMutation

data class IngredientModification(
    val id: String,
    override val name: String?,
    override val description: String?,
    override val imageIds: List<String>?,
) : IngredientMutation

package co.digamma.ca.domain.api.cookbook

import co.digamma.ca.domain.api.CrudService
import co.digamma.ca.domain.api.model.ModelReference
import java.util.Locale

interface IngredientService : CrudService<Ingredient, IngredientCreation, IngredientModification>

interface IngredientMutation {
    val name: String?
    val description: String?
    val imageIds: List<String>?
}

data class IngredientCreation(
    val locale: Locale,
    override val name: String,
    override val description: String,
    override val imageIds: List<String> = emptyList(),
) : IngredientMutation

data class IngredientModification(
    override val id: String,
    override val name: String?,
    override val description: String?,
    override val imageIds: List<String>?,
) : IngredientMutation, ModelReference<Ingredient>

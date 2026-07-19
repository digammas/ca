package co.digamma.ca.domain.internal.cookbook

import co.digamma.ca.domain.api.common.stereotypes.Singleton
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientCreation
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientModification
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredientService
import co.digamma.ca.domain.internal.DefaultCurdService
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.domain.spi.cookbook.RecipeRepository

@Singleton
open class DomainQuantifiedIngredientService(
    override val repository: QuantifiedIngredientRepository,
    private val ingredientRepository: IngredientRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val recipeRepository: RecipeRepository,
) :
    DefaultCurdService<QuantifiedIngredient, QuantifiedIngredientCreation, QuantifiedIngredientModification>(),
    QuantifiedIngredientService {

    override fun toModel(creation: QuantifiedIngredientCreation): QuantifiedIngredient {
        val recipeId = creation.recipeId ?: throw IllegalArgumentException("recipeId is required")
        return QuantifiedIngredient(
            id = generateId(),
            ingredient = ingredientRepository.retrieveOrThrow(creation.ingredientId),
            quantity = creation.quantity,
            unit = measurementUnitRepository.retrieveOrThrow(creation.measurementUnitId),
            recipe = recipeRepository.retrieveOrThrow(recipeId),
        )
    }

    override fun toModel(
        modification: QuantifiedIngredientModification,
        existing: QuantifiedIngredient,
    ) = QuantifiedIngredient(
        id = existing.id,
        ingredient = existing.ingredient,
        quantity = modification.quantity ?: existing.quantity,
        unit = modification.measurementUnitId
            ?.let { measurementUnitRepository.retrieveOrThrow(it) }
            ?: existing.unit,
        recipe = existing.recipe,
    )

    override fun retrieveByRecipe(recipeId: String): List<QuantifiedIngredient> {
        return this.repository.retrieveByRecipe(recipeId)
    }
}


package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.suites.food.givenQuantifiedIngredient
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

abstract class QuantifiedIngredientRepositoryTestBase : CrudRepositoryTestBase<QuantifiedIngredient>() {

    abstract override val sut: QuantifiedIngredientRepository
    abstract val ingredientRepository: IngredientRepository
    abstract val measurementUnitRepository: MeasurementUnitRepository
    abstract val recipeRepository: RecipeRepository
    abstract val dishRepository: DishRepository
    abstract val courseRepository: CourseRepository
    abstract val cuisineRepository: CuisineRepository
    abstract val servingRepository: ServingRepository
    abstract val userRepository: UserRepository

    override fun newModel() = givenQuantifiedIngredient().also {
        courseRepository.create(it.recipe.dish.course)
        cuisineRepository.create(it.recipe.dish.cuisine)
        servingRepository.create(it.recipe.dish.serving)
        dishRepository.create(it.recipe.dish)
        userRepository.create(it.recipe.author)
        recipeRepository.create(it.recipe)
        measurementUnitRepository.create(it.unit)
        ingredientRepository.create(it.ingredient)
    }

    override fun modifyModel(model: QuantifiedIngredient) = QuantifiedIngredient(
        id = model.id,
        ingredient = model.ingredient,
        quantity = model.quantity + 1f,
        unit = model.unit,
        recipe = model.recipe,
    )

    @Test
    fun `test retrieve by recipe empty`() {
        val quantifiedIngredient = newModel()
        val retrieved = sut.retrieveByRecipe(quantifiedIngredient.recipe.id)
        assertTrue(retrieved.isEmpty())
    }

    @Test
    fun `test retrieve by recipe`() {
        val quantifiedIngredient = sut.create(newModel())
        val sibling = sut.create(
            givenQuantifiedIngredient().copy(
                recipe = quantifiedIngredient.recipe,
                ingredient = quantifiedIngredient.ingredient,
                unit = quantifiedIngredient.unit,
            )
        )
        sut.create(newModel())
        val retrieved = sut.retrieveByRecipe(quantifiedIngredient.recipe.id)
        assertEquals(setOf(quantifiedIngredient.id, sibling.id), retrieved.map { it.id }.toSet())
    }
}


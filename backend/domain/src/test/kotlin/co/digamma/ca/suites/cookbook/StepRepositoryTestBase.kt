package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.PageSpecs
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.fixtures.givenString
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.util.Locale
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

abstract class StepRepositoryTestBase : CrudRepositoryTestBase<Step>() {

    abstract override val sut: StepRepository
    abstract val recipeRepository: RecipeRepository
    abstract val dishRepository: DishRepository
    abstract val courseRepository: CourseRepository
    abstract val cuisineRepository: CuisineRepository
    abstract val servingRepository: ServingRepository
    abstract val userRepository: UserRepository

    override fun newModel() = givenStep().also {
        courseRepository.create(it.recipe.dish.course)
        cuisineRepository.create(it.recipe.dish.cuisine)
        servingRepository.create(it.recipe.dish.serving)
        dishRepository.create(it.recipe.dish)
        userRepository.create(it.recipe.author)
        recipeRepository.create(it.recipe)
    }

    override fun modifyModel(model: Step) = Step(
        id = model.id,
        locale = Locale.FRENCH,
        description = givenString(40),
        estimatedTime = model.estimatedTime + 5,
        recipe = model.recipe,
    )

    private val pageSpecs = PageSpecs(0, 10)

    @Test
    fun `test retrieve by recipe`() {
        val step = sut.create(newModel())
        sut.create(newModel())
        val retrieved = sut.retrieveByRecipe(step.recipe.id, pageSpecs)
        assertEquals(listOf(step.id), retrieved.results.map { it.id })
    }

    @Test
    fun `test retrieve by recipe empty`() {
        val step = newModel()
        val retrieved = sut.retrieveByRecipe(step.recipe.id, pageSpecs)
        assertTrue(retrieved.results.isEmpty())
    }
}


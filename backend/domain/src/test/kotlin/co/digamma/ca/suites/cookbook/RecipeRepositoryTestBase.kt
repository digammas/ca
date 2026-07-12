package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.time.Instant
import java.util.Locale

abstract class RecipeRepositoryTestBase : CrudRepositoryTestBase<Recipe>() {

    abstract override val sut: RecipeRepository
    abstract val dishRepository: DishRepository
    abstract val courseRepository: CourseRepository
    abstract val cuisineRepository: CuisineRepository
    abstract val servingRepository: ServingRepository
    abstract val userRepository: UserRepository

    override fun newModel() = giveRecipe().also {
        courseRepository.create(it.dish.course)
        cuisineRepository.create(it.dish.cuisine)
        servingRepository.create(it.dish.serving)
        dishRepository.create(it.dish)
        userRepository.create(it.author)
    }

    override fun modifyModel(model: Recipe) = Recipe(
        id = model.id,
        locale = Locale.FRENCH,
        dish = model.dish,
        ingredients = model.ingredients,
        steps = model.steps,
        yield = model.yield + 2,
        createdAt = model.createdAt,
        updatedAt = Instant.now(),
        author = model.author,
        images = model.images,
        timeToServe = (model.timeToServe ?: 30) + 5,
    )
}


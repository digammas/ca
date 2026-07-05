package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Ingredient
import co.digamma.ca.domain.api.cookbook.MeasurementUnit
import co.digamma.ca.domain.api.cookbook.QuantifiedIngredient
import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.spi.cookbook.IngredientRepository
import co.digamma.ca.domain.spi.cookbook.MeasurementUnitRepository
import co.digamma.ca.domain.spi.cookbook.QuantifiedIngredientRepository
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.time.Instant
import java.util.Locale

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

    protected fun newCourse(): Course {
        val course = Course(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
        )
        return courseRepository.create(course)
    }

    protected fun newCuisine(): Cuisine {
        val cuisine = Cuisine(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
        )
        return cuisineRepository.create(cuisine)
    }

    protected fun newServing(): Serving {
        val serving = Serving(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
        )
        return servingRepository.create(serving)
    }

    protected fun newDish(): Dish {
        val dish = Dish(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
            course = newCourse(),
            cuisine = newCuisine(),
            serving = newServing(),
        )
        return dishRepository.create(dish)
    }

    protected fun newUser(): User {
        val user = User(
            username = RandGen.string(12),
            password = RandGen.string(24),
            email = "${RandGen.string(8)}@example.com",
        )
        return userRepository.create(user)
    }

    protected fun newRecipe(): Recipe {
        val recipe = Recipe(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            dish = newDish(),
            yield = 4,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            author = newUser(),
            timeToServe = 30,
        )
        return recipeRepository.create(recipe)
    }

    protected fun newIngredient(): Ingredient {
        val ingredient = Ingredient(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
            description = RandGen.string(),
        )
        return ingredientRepository.create(ingredient)
    }

    protected fun newMeasurementUnit(): MeasurementUnit {
        val unit = MeasurementUnit(
            id = RandGen.uuid(),
            locale = Locale.ENGLISH,
            name = RandGen.string(),
            dimension = MeasurementUnit.Dimension.QUANTITY,
        )
        return measurementUnitRepository.create(unit)
    }

    override fun newModel() = QuantifiedIngredient(
        id = RandGen.uuid(),
        ingredient = newIngredient(),
        quantity = 3.5f,
        unit = newMeasurementUnit(),
        recipe = newRecipe(),
    )

    override fun modifyModel(model: QuantifiedIngredient) = QuantifiedIngredient(
        id = model.id,
        ingredient = model.ingredient,
        quantity = model.quantity + 1f,
        unit = model.unit,
        recipe = model.recipe,
    )
}


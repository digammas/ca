package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.api.cookbook.Step
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.api.users.User
import co.digamma.ca.domain.spi.cookbook.RecipeRepository
import co.digamma.ca.domain.spi.cookbook.StepRepository
import co.digamma.ca.domain.spi.food.CourseRepository
import co.digamma.ca.domain.spi.food.CuisineRepository
import co.digamma.ca.domain.spi.food.DishRepository
import co.digamma.ca.domain.spi.food.ServingRepository
import co.digamma.ca.domain.spi.users.UserRepository
import co.digamma.ca.fixtures.utils.RandGen
import co.digamma.ca.suites.persistence.CrudRepositoryTestBase
import java.time.Instant
import java.util.Locale

abstract class StepRepositoryTestBase : CrudRepositoryTestBase<Step>() {

    abstract override val sut: StepRepository
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

    override fun newModel() = Step(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        description = RandGen.string(32),
        estimatedTime = 10,
        recipe = newRecipe(),
    )

    override fun modifyModel(model: Step) = Step(
        id = model.id,
        locale = Locale.FRENCH,
        description = RandGen.string(40),
        estimatedTime = model.estimatedTime + 5,
        recipe = model.recipe,
    )
}


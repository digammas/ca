package co.digamma.ca.suites.cookbook

import co.digamma.ca.domain.api.cookbook.Recipe
import co.digamma.ca.domain.api.food.Course
import co.digamma.ca.domain.api.food.Cuisine
import co.digamma.ca.domain.api.food.Dish
import co.digamma.ca.domain.api.food.Serving
import co.digamma.ca.domain.api.users.User
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

abstract class RecipeRepositoryTestBase : CrudRepositoryTestBase<Recipe>() {

    abstract override val sut: RecipeRepository
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

    override fun newModel() = Recipe(
        id = RandGen.uuid(),
        locale = Locale.ENGLISH,
        dish = newDish(),
        yield = 4,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
        author = newUser(),
        timeToServe = 45,
    )

    override fun modifyModel(model: Recipe) = Recipe(
        id = model.id,
        locale = Locale.FRENCH,
        dish = newDish(),
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

